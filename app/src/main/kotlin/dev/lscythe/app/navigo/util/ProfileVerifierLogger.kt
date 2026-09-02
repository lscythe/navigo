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
package dev.lscythe.app.navigo.util

import androidx.profileinstaller.ProfileVerifier
import androidx.profileinstaller.ProfileVerifier.CompilationStatus.RESULT_CODE_PROFILE_ENQUEUED_FOR_COMPILATION
import co.touchlab.kermit.Severity
import dev.lscythe.app.navigo.core.common.coroutines.ApplicationScope
import dev.lscythe.app.navigo.core.monitoring.AppLogger
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

@Inject
class ProfileVerifierLogger(
    @ApplicationScope private val scope: CoroutineScope,
    private val logger: AppLogger,
) {
    operator fun invoke() {
        scope.launch {
            val status = ProfileVerifier.getCompilationStatusAsync().await()
            val state =
                when {
                    status.isCompiledWithProfile -> "compiled"
                    status.profileInstallResultCode ==
                        RESULT_CODE_PROFILE_ENQUEUED_FOR_COMPILATION -> "enqueued"
                    else -> "not_compiled"
                }
            logger.log(
                severity = Severity.Debug,
                message = "Baseline profile status",
                attributes =
                    mapOf(
                        "resultCode" to status.profileInstallResultCode,
                        "state" to state,
                    ),
            )
        }
    }
}
