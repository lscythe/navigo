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
import dev.lscythe.app.navigo.core.datastore.samplePreference
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class SamplePreferenceSerializerTest :
    FunSpec({
        test("defaultValue is an empty SamplePreference") {
            SamplePreferenceSerializer.defaultValue shouldBe samplePreference {}
        }

        test("writeTo then readFrom round-trips the value") {
            val original = samplePreference {
                id = 1
                name = "navigo"
            }
            val output = ByteArrayOutputStream()
            SamplePreferenceSerializer.writeTo(original, output)

            val result =
                SamplePreferenceSerializer.readFrom(ByteArrayInputStream(output.toByteArray()))

            result shouldBe original
        }

        test("readFrom throws CorruptionException on invalid bytes") {
            shouldThrow<CorruptionException> {
                SamplePreferenceSerializer.readFrom(ByteArrayInputStream(byteArrayOf(-1, -2, -3)))
            }
        }
    })
