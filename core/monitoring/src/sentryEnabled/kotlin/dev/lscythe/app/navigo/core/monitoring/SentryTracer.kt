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
package dev.lscythe.app.navigo.core.monitoring

import dev.zacsweers.metro.Inject
import io.sentry.Sentry
import io.sentry.SpanStatus

internal class SentryTracer @Inject constructor() : Tracer {
    override fun <T> trace(name: String, operation: String, block: () -> T): T {
        val transaction = Sentry.startTransaction(name, operation)
        return try {
            block().also { transaction.status = SpanStatus.OK }
        } catch (throwable: Throwable) {
            transaction.throwable = throwable
            transaction.status = SpanStatus.INTERNAL_ERROR
            throw throwable
        } finally {
            transaction.finish()
        }
    }
}
