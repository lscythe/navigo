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

import dev.lscythe.app.navigo.BuildConfig
import dev.lscythe.app.navigo.domain.auth.AuthEvidenceProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

@ContributesTo(AppScope::class)
@BindingContainer
object AuthProviderBindings {
    @Provides
    fun provideAuthEvidenceProvider(): AuthEvidenceProvider =
        DevelopmentEvidenceProvider(
            packageName = BuildConfig.APPLICATION_ID.removeSuffix(".debug"),
            signerDigest = BuildConfig.DEVELOPMENT_SIGNER_DIGEST,
            keyId = BuildConfig.DEVELOPMENT_KEY_ID,
            privateKeySeed = BuildConfig.DEVELOPMENT_PRIVATE_KEY_SEED,
            evidenceExpiresAt = { Clock.System.now() + 1.minutes },
        )
}
