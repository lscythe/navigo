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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.Foundation.NSError
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatus
import platform.UserNotifications.UNUserNotificationCenter
import platform.darwin.NSObject

@Composable
actual fun rememberPermissionRequester(): PermissionRequester = remember {
    IosPermissionRequester()
}

private class IosPermissionRequester : PermissionRequester {
    private val locationManager = CLLocationManager()
    private var locationDelegate: LocationDelegate? = null

    override suspend fun request(permission: AppPermission): PermissionResult =
        when (permission) {
            AppPermission.Location -> requestLocation()
            AppPermission.Notifications -> requestNotifications()
        }

    private suspend fun requestLocation(): PermissionResult {
        locationStatus(CLLocationManager.authorizationStatus())?.let {
            return it
        }
        return suspendCancellableCoroutine { continuation ->
            val delegate = LocationDelegate { status ->
                locationStatus(status)?.let { result ->
                    locationDelegate = null
                    locationManager.delegate = null
                    if (continuation.isActive) continuation.resume(result)
                }
            }
            locationDelegate = delegate
            locationManager.delegate = delegate
            continuation.invokeOnCancellation {
                locationManager.delegate = null
                locationDelegate = null
            }
            locationManager.requestWhenInUseAuthorization()
        }
    }

    private suspend fun requestNotifications(): PermissionResult =
        suspendCancellableCoroutine { continuation ->
            val center = UNUserNotificationCenter.currentNotificationCenter()
            center.getNotificationSettingsWithCompletionHandler { settings ->
                val current = settings?.let { notificationStatus(it.authorizationStatus) }
                if (current != null) {
                    if (continuation.isActive) continuation.resume(current)
                    return@getNotificationSettingsWithCompletionHandler
                }
                center.requestAuthorizationWithOptions(
                    UNAuthorizationOptionAlert or
                        UNAuthorizationOptionBadge or
                        UNAuthorizationOptionSound
                ) { granted, _: NSError? ->
                    if (continuation.isActive) {
                        continuation.resume(
                            if (granted) PermissionResult.Granted else PermissionResult.Denied
                        )
                    }
                }
            }
        }
}

private class LocationDelegate(private val onStatusChanged: (CLAuthorizationStatus) -> Unit) :
    NSObject(), CLLocationManagerDelegateProtocol {
    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        onStatusChanged(manager.authorizationStatus)
    }
}

private fun locationStatus(status: CLAuthorizationStatus): PermissionResult? =
    when (status) {
        platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways,
        platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse -> PermissionResult.Granted
        platform.CoreLocation.kCLAuthorizationStatusDenied -> PermissionResult.Denied
        platform.CoreLocation.kCLAuthorizationStatusRestricted -> PermissionResult.Restricted
        platform.CoreLocation.kCLAuthorizationStatusNotDetermined -> null
        else -> PermissionResult.NotRequired
    }

private fun notificationStatus(status: UNAuthorizationStatus): PermissionResult? =
    when (status) {
        platform.UserNotifications.UNAuthorizationStatusAuthorized,
        platform.UserNotifications.UNAuthorizationStatusProvisional,
        platform.UserNotifications.UNAuthorizationStatusEphemeral -> PermissionResult.Granted
        platform.UserNotifications.UNAuthorizationStatusDenied -> PermissionResult.Denied
        platform.UserNotifications.UNAuthorizationStatusNotDetermined -> null
        else -> PermissionResult.NotRequired
    }
