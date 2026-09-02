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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.tracing.trace
import dev.lscythe.app.navigo.core.common.coroutines.ApplicationScope
import dev.lscythe.app.navigo.core.common.dispatchers.Dispatcher
import dev.lscythe.app.navigo.core.common.dispatchers.NavigoDispatchers
import dev.lscythe.app.navigo.core.common.time.TimeZoneMonitor
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.time.ZoneId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinTimeZone

@Inject
@SingleIn(AppScope::class)
internal class TimeZoneBroadcastMonitor
constructor(
    private val context: Context,
    @ApplicationScope appScope: CoroutineScope,
    @Dispatcher(NavigoDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : TimeZoneMonitor {

    override val currentTimeZone: SharedFlow<TimeZone> = callbackFlow {
        trySend(TimeZone.currentSystemDefault())

        val receiver =
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action != Intent.ACTION_TIMEZONE_CHANGED) return

                    val zoneIdFromIntent =
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                            null
                        } else {
                            intent.getStringExtra(Intent.EXTRA_TIMEZONE)?.let { timeZoneId ->
                                val zoneId = ZoneId.of(timeZoneId, ZoneId.SHORT_IDS)
                                zoneId.toKotlinTimeZone()
                            }
                        }

                    trySend(zoneIdFromIntent ?: TimeZone.currentSystemDefault())
                }
            }

        trace("TimeZoneBroadcastReceiver.register") {
            context.registerReceiver(receiver, IntentFilter(Intent.ACTION_TIMEZONE_CHANGED))
        }
        trySend(TimeZone.currentSystemDefault())

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }
        .distinctUntilChanged()
        .conflate()
        .flowOn(ioDispatcher)
        .shareIn(appScope, SharingStarted.WhileSubscribed(5_000), 1)
}
