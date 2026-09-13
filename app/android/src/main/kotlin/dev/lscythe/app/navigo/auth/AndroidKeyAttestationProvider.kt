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

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyInfo
import android.security.keystore.KeyProperties
import android.security.keystore.StrongBoxUnavailableException
import dev.lscythe.app.navigo.core.persistence.AttestationEnrollmentPreference
import dev.lscythe.app.navigo.core.persistence.datasource.AttestationEnrollmentDataSource
import dev.lscythe.app.navigo.data.auth.evidence.androidAssertionData
import dev.lscythe.app.navigo.domain.auth.AuthEvidenceProvider
import dev.lscythe.app.navigo.domain.auth.AuthFailure
import dev.lscythe.app.navigo.domain.auth.AuthResult
import dev.lscythe.app.navigo.domain.auth.model.AuthChallenge
import dev.lscythe.app.navigo.domain.auth.model.AuthProvider
import dev.lscythe.app.navigo.domain.auth.model.EnrollmentEvidence
import dev.lscythe.app.navigo.domain.auth.model.SessionEvidence
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.MessageDigest
import java.security.Signature
import java.security.spec.ECGenParameterSpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.flow.first

private const val ANDROID_KEYSTORE = "AndroidKeyStore"
private const val DEFAULT_KEY_ALIAS = "navigo-attestation"

internal data class AttestationEnrollment(
    val enrollmentId: String,
    val keyAlias: String,
    val publicKeyId: String,
)

internal interface AttestationEnrollmentStore {
    var enrollment: AttestationEnrollment?
        get() = null
        set(_) = Unit

    suspend fun current(): AttestationEnrollment? = enrollment

    suspend fun save(enrollment: AttestationEnrollment)

    suspend fun clear()
}

internal class PersistentAttestationEnrollmentStore(
    private val dataSource: AttestationEnrollmentDataSource
) : AttestationEnrollmentStore {
    override suspend fun current(): AttestationEnrollment? =
        dataSource.data.first().takeUnless { it.enrollmentId.isEmpty() }?.toEnrollment()

    override suspend fun save(enrollment: AttestationEnrollment) {
        dataSource.setEnrollment(enrollment.toPreference())
    }

    override suspend fun clear() {
        dataSource.clearEnrollment()
    }
}

internal data class AndroidAttestationKey(
    val certificateChainDer: List<ByteArray>,
    val publicKeySpkiDer: ByteArray,
)

internal interface AndroidAttestationKeyStore {
    fun generate(alias: String, challenge: ByteArray, strongBox: Boolean): AndroidAttestationKey

    fun signDigest(alias: String, digest: ByteArray): ByteArray

    fun delete(alias: String)
}

@OptIn(ExperimentalEncodingApi::class)
internal class AndroidKeyAttestationProvider(
    private val enrollmentStore: AttestationEnrollmentStore,
    private val keyStore: AndroidAttestationKeyStore,
) : AuthEvidenceProvider {
    override val provider = AuthProvider.AndroidKeyAttestation
    private var pendingEnrollment: AttestationEnrollment? = null

    override suspend fun needsEnrollment(): Boolean = enrollmentStore.current() == null

    override suspend fun createEnrollmentEvidence(
        challenge: AuthChallenge
    ): AuthResult<EnrollmentEvidence> =
        try {
            val encoding = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT)
            val challengeBytes = encoding.decode(challenge.nonce)
            val key =
                try {
                    keyStore.generate(DEFAULT_KEY_ALIAS, challengeBytes, strongBox = true)
                } catch (_: StrongBoxUnavailableException) {
                    keyStore.generate(DEFAULT_KEY_ALIAS, challengeBytes, strongBox = false)
                }
            val publicKeyId =
                encoding.encode(MessageDigest.getInstance("SHA-256").digest(key.publicKeySpkiDer))
            pendingEnrollment = AttestationEnrollment("", DEFAULT_KEY_ALIAS, publicKeyId)
            AuthResult.Success(
                EnrollmentEvidence.AndroidKeyAttestation(
                    certificateChain = key.certificateChainDer.map(encoding::encode),
                    publicKeyId = publicKeyId,
                )
            )
        } catch (_: Exception) {
            keyStore.delete(DEFAULT_KEY_ALIAS)
            pendingEnrollment = null
            AuthResult.Failure(AuthFailure.InvalidEvidence())
        }

    override suspend fun enrollmentCreated(enrollmentId: String) {
        val pending = requireNotNull(pendingEnrollment)
        enrollmentStore.save(pending.copy(enrollmentId = enrollmentId))
        pendingEnrollment = null
    }

    override suspend fun enrollmentRevoked() {
        val enrollment = enrollmentStore.current()
        if (enrollment != null) keyStore.delete(enrollment.keyAlias)
        enrollmentStore.clear()
    }

    override suspend fun createSessionEvidence(
        challenge: AuthChallenge
    ): AuthResult<SessionEvidence> {
        val enrollment =
            enrollmentStore.current() ?: return AuthResult.Failure(AuthFailure.EnrollmentRevoked())
        val canonical = androidAssertionData(challenge, enrollment.enrollmentId)
        val digest = MessageDigest.getInstance("SHA-256").digest(canonical)
        val encoding = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT)
        return AuthResult.Success(
            SessionEvidence.AndroidKeyAttestation(
                enrollmentId = enrollment.enrollmentId,
                clientData = encoding.encode(canonical),
                signature = encoding.encode(keyStore.signDigest(enrollment.keyAlias, digest)),
            )
        )
    }
}

internal class PlatformAndroidAttestationKeyStore : AndroidAttestationKeyStore {
    override fun generate(
        alias: String,
        challenge: ByteArray,
        strongBox: Boolean,
    ): AndroidAttestationKey {
        delete(alias)
        val specification =
            KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN)
                .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
                .setDigests(KeyProperties.DIGEST_NONE)
                .setAttestationChallenge(challenge)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                        setIsStrongBoxBacked(strongBox)
                }
                .build()
        val keyPair =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, ANDROID_KEYSTORE).run {
                initialize(specification)
                generateKeyPair()
            }
        val keyFactory = KeyFactory.getInstance(keyPair.private.algorithm, ANDROID_KEYSTORE)
        val keyInfo = keyFactory.getKeySpec(keyPair.private, KeyInfo::class.java)
        check(
            keyInfo.securityLevel == KeyProperties.SECURITY_LEVEL_TRUSTED_ENVIRONMENT ||
                keyInfo.securityLevel == KeyProperties.SECURITY_LEVEL_STRONGBOX
        )
        val chain = keyStore().getCertificateChain(alias).map { it.encoded }
        return AndroidAttestationKey(chain, keyPair.public.encoded)
    }

    override fun signDigest(alias: String, digest: ByteArray): ByteArray {
        val privateKey = keyStore().getKey(alias, null)
        return Signature.getInstance("NONEwithECDSA").run {
            initSign(privateKey as java.security.PrivateKey)
            update(digest)
            sign()
        }
    }

    override fun delete(alias: String) {
        keyStore().deleteEntry(alias)
    }

    private fun keyStore() = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
}

private fun AttestationEnrollmentPreference.toEnrollment() =
    AttestationEnrollment(enrollmentId, keyAlias, publicKeyId)

private fun AttestationEnrollment.toPreference() =
    AttestationEnrollmentPreference(enrollmentId, keyAlias, publicKeyId)
