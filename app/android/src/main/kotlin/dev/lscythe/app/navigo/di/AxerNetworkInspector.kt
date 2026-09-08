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
package dev.lscythe.app.navigo.di

import dev.lscythe.app.navigo.core.network.NetworkInspector
import io.github.orioneee.Axer
import io.ktor.client.HttpClientConfig
import io.ktor.http.HttpHeaders

class AxerNetworkInspector : NetworkInspector {
    override fun install(config: HttpClientConfig<*>) {
        config.install(Axer.ktorPlugin) {
            requestReducer = { it.copy(headers = it.headers.redacted()) }
            responseReducer = { it.copy(headers = it.headers.redacted()) }
        }
    }
}

private fun Map<String, String>.redacted() = mapValues { (name, value) ->
    if (
        name.equals(HttpHeaders.Authorization, true) ||
            name.equals(HttpHeaders.Cookie, true) ||
            name.equals(HttpHeaders.SetCookie, true)
    )
        "***"
    else value
}
