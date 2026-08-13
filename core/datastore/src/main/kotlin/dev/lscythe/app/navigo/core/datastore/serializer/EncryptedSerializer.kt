/*
 * Copyright 2026 Lscythe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.lscythe.app.navigo.core.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.GeneralSecurityException

private val NO_ASSOCIATED_DATA = ByteArray(0)

/**
 * Wraps [delegate] so its serialized bytes are encrypted at rest via Tink's [Aead], transparent to
 * callers of the resulting DataStore.
 */
class EncryptedSerializer<T>(private val delegate: Serializer<T>, private val aead: Aead) :
    Serializer<T> {

    override val defaultValue: T = delegate.defaultValue

    override suspend fun readFrom(input: InputStream): T {
        val ciphertext = input.readBytes()
        if (ciphertext.isEmpty()) return delegate.defaultValue

        val plaintext =
            try {
                aead.decrypt(ciphertext, NO_ASSOCIATED_DATA)
            } catch (exception: GeneralSecurityException) {
                throw CorruptionException("Cannot decrypt data.", exception)
            }

        return delegate.readFrom(ByteArrayInputStream(plaintext))
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        val plaintext = ByteArrayOutputStream().also { delegate.writeTo(t, it) }.toByteArray()
        output.write(aead.encrypt(plaintext, NO_ASSOCIATED_DATA))
    }
}
