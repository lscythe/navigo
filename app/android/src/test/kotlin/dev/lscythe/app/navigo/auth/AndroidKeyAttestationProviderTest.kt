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
package dev.lscythe.app.navigo.auth

import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.model.AuthAction
import dev.lscythe.app.navigo.domain.auth.model.AuthChallenge
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.EnrollmentEvidence
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Instant

class AndroidKeyAttestationProviderTest :
    FunSpec({
        test("creates leaf-first enrollment evidence and persists only after server success") {
            withProvider { provider, keys, store ->
                provider.needsEnrollment() shouldBe true
                provider.createEnrollmentEvidence(challenge(AuthAction.EnrollInstallation)) shouldBe
                    AuthResult.Success(
                        EnrollmentEvidence.AndroidKeyAttestation(
                            certificateChain = listOf("bGVhZg", "cm9vdA"),
                            publicKeyId = "D3ww1v8Rm1W43c7GGrtdv-oX2zVM5V0a8o2EZtJ7haU",
                        )
                    )
                keys.generatedChallenge shouldBe ByteArray(32) { (it + 32).toByte() }.toList()
                keys.strongBoxRequested shouldBe true
                store.enrollment shouldBe null

                provider.enrollmentCreated("enrollment")
                store.enrollment shouldBe
                    AttestationEnrollment(
                        enrollmentId = "enrollment",
                        keyAlias = "navigo-attestation",
                        publicKeyId = "D3ww1v8Rm1W43c7GGrtdv-oX2zVM5V0a8o2EZtJ7haU",
                    )
            }
        }

        test("creates exact client data and signs its SHA-256 digest") {
            withProvider(
                initial =
                    AttestationEnrollment(
                        "enrollment-android-001",
                        "persisted-alias",
                        "key-id",
                    )
            ) { provider, keys, _ ->
                provider.createSessionEvidence(challenge(AuthAction.CreateSession)) shouldBe
                    AuthResult.Success(
                        SessionEvidence.AndroidKeyAttestation(
                            enrollmentId = "enrollment-android-001",
                            clientData =
                                "eyJwcm90b2NvbFZlcnNpb24iOiJ2MiIsInByb3ZpZGVyIjoiYW5kcm9pZC1rZXktYXR0ZXN0YXRpb24iLCJhY3Rpb24iOiJjcmVhdGUtc2Vzc2lvbiIsImNoYWxsZW5nZUlkIjoiY2hhbGxlbmdlLWFuZHJvaWQtMDAxIiwibm9uY2UiOiJJQ0VpSXlRbEppY29LU29yTEMwdUx6QXhNak0wTlRZM09EazZPenc5UGo4IiwiZW5yb2xsbWVudElkIjoiZW5yb2xsbWVudC1hbmRyb2lkLTAwMSIsImV4cGlyZXNBdCI6IjIwMjYtMDktMDhUMTM6MDI6MDAuMTIzWiJ9",
                            signature = "c2lnbmF0dXJl",
                        )
                    )
                keys.signedAlias shouldBe "persisted-alias"
                keys.signedDigest?.toHex() shouldBe
                    "27e4f3cb9e9f1700413b7bd4479ebccdf874f5a7cdfb31166febbaafb424aecd"
            }
        }

        test("revocation deletes key and enrollment") {
            withProvider(initial = AttestationEnrollment("enrollment", "alias", "key")) {
                provider,
                keys,
                source ->
                provider.enrollmentRevoked()

                keys.deletedAliases shouldBe listOf("alias")
                source.enrollment shouldBe null
            }
        }

        test("session evidence requires a stored enrollment") {
            withProvider { provider, _, _ ->
                provider.createSessionEvidence(challenge(AuthAction.CreateSession)) shouldBe
                    AuthResult.Failure(AuthFailure.EnrollmentRevoked())
            }
        }
    })

private suspend fun withProvider(
    initial: AttestationEnrollment? = null,
    block:
        suspend (
            AndroidKeyAttestationProvider,
            FakeAndroidAttestationKeyStore,
            FakeAttestationEnrollmentStore,
        ) -> Unit,
) {
    val store = FakeAttestationEnrollmentStore(initial)
    val keys = FakeAndroidAttestationKeyStore()
    block(AndroidKeyAttestationProvider(store, keys), keys, store)
}

private class FakeAttestationEnrollmentStore(override var enrollment: AttestationEnrollment?) :
    AttestationEnrollmentStore {
    override suspend fun save(enrollment: AttestationEnrollment) {
        this.enrollment = enrollment
    }

    override suspend fun clear() {
        enrollment = null
    }
}

private class FakeAndroidAttestationKeyStore : AndroidAttestationKeyStore {
    var generatedChallenge: List<Byte>? = null
    var strongBoxRequested = false
    var signedAlias: String? = null
    var signedDigest: ByteArray? = null
    val deletedAliases = mutableListOf<String>()

    override fun generate(
        alias: String,
        challenge: ByteArray,
        strongBox: Boolean,
    ): AndroidAttestationKey {
        generatedChallenge = challenge.toList()
        strongBoxRequested = strongBox
        return AndroidAttestationKey(
            certificateChainDer = listOf("leaf".encodeToByteArray(), "root".encodeToByteArray()),
            publicKeySpkiDer = "public-key-id".encodeToByteArray(),
        )
    }

    override fun signDigest(alias: String, digest: ByteArray): ByteArray {
        signedAlias = alias
        signedDigest = digest
        return "signature".encodeToByteArray()
    }

    override fun delete(alias: String) {
        deletedAliases += alias
    }
}

private fun challenge(action: AuthAction) =
    AuthChallenge(
        id = "challenge-android-001",
        nonce = "ICEiIyQlJicoKSorLC0uLzAxMjM0NTY3ODk6Ozw9Pj8",
        provider = AuthProvider.AndroidKeyAttestation,
        action = action,
        protocolVersion = "v2",
        expiresAt = Instant.parse("2026-09-08T13:02:00.123Z"),
    )

private fun ByteArray.toHex(): String = joinToString("") { byte -> "%02x".format(byte) }
