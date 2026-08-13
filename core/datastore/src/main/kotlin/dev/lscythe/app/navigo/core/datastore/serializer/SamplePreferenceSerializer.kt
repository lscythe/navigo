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
import com.google.protobuf.InvalidProtocolBufferException
import dev.lscythe.app.navigo.core.datastore.SamplePreference
import java.io.InputStream
import java.io.OutputStream

object SamplePreferenceSerializer : Serializer<SamplePreference> {

    override val defaultValue: SamplePreference = SamplePreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SamplePreference =
        try {
            SamplePreference.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read SamplePreference.", exception)
        }

    override suspend fun writeTo(t: SamplePreference, output: OutputStream) = t.writeTo(output)
}
