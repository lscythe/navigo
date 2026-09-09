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
package dev.lscythe.app.navigo.core.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine

@Composable
actual fun rememberPermissionRequester(): PermissionRequester {
    val context = LocalContext.current
    var continuation by remember {
        mutableStateOf<CancellableContinuation<PermissionResult>?>(null)
    }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            grants ->
            continuation?.let { pending ->
                continuation = null
                if (pending.isActive) {
                    pending.resume(
                        if (grants.isNotEmpty() && grants.values.any { it }) {
                            PermissionResult.Granted
                        } else {
                            PermissionResult.Denied
                        }
                    )
                }
            }
        }
    return remember(context, launcher) {
        AndroidPermissionRequester(context) { permissions ->
            suspendCancellableCoroutine { pending ->
                check(continuation == null) { "A permission request is already active" }
                continuation = pending
                pending.invokeOnCancellation {
                    if (continuation === pending) continuation = null
                }
                launcher.launch(permissions)
            }
        }
    }
}

private class AndroidPermissionRequester(
    private val context: Context,
    private val launch: suspend (Array<String>) -> PermissionResult,
) : PermissionRequester {
    override suspend fun request(permission: AppPermission): PermissionResult {
        val permissions = permission.androidPermissions()
        if (permissions.isEmpty()) return PermissionResult.NotRequired
        if (permissions.any { context.isGranted(it) }) return PermissionResult.Granted
        return launch(permissions)
    }
}

private fun AppPermission.androidPermissions(): Array<String> =
    when (this) {
        AppPermission.Location ->
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        AppPermission.Notifications ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                emptyArray()
            }
    }

private fun Context.isGranted(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
