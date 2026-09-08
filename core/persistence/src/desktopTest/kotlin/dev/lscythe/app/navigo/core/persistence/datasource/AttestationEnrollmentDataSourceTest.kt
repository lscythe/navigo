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
package dev.lscythe.app.navigo.core.persistence.datasource

import dev.lscythe.app.navigo.core.persistence.AttestationEnrollmentPreference
import eu.anifantakis.lib.ksafe.KSafe
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

class AttestationEnrollmentDataSourceTest :
    FunSpec({
        test("enrollment defaults empty") {
            withDataSource("empty") {
                data.first() shouldBe AttestationEnrollmentPreference()
            }
        }

        test("enrollment writes and replaces one complete record") {
            withDataSource("replace") {
                setEnrollment(AttestationEnrollmentPreference("first", "alias-one", "key-one"))
                setEnrollment(AttestationEnrollmentPreference("second", "alias-two", "key-two"))

                data.first() shouldBe
                    AttestationEnrollmentPreference("second", "alias-two", "key-two")
            }
        }

        test("enrollment clear removes the complete record") {
            withDataSource("clear") {
                setEnrollment(AttestationEnrollmentPreference("enrollment", "alias", "key"))
                clearEnrollment()

                data.first() shouldBe AttestationEnrollmentPreference()
            }
        }
    })

private suspend fun withDataSource(
    suffix: String,
    block: suspend AttestationEnrollmentDataSource.() -> Unit,
) {
    val ksafe = KSafe(fileName = "navigo_test_attestation_enrollment_$suffix")
    try {
        AttestationEnrollmentDataSource(ksafe).block()
    } finally {
        ksafe.clearAll()
        ksafe.close()
    }
}
