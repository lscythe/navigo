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
package dev.lscythe.app.navigo.core.datastore.datasource

import dev.lscythe.app.navigo.core.datastore.samplePreference
import dev.lscythe.app.navigo.core.datastore.test.InMemoryDataStore
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

class NavigoPreferenceDataSourceTest :
    FunSpec({
        test("data reflects the underlying DataStore") {
            val dataStore = InMemoryDataStore(samplePreference { name = "initial" })
            val dataSource = NavigoPreferenceDataSource(dataStore)

            dataSource.data.first().name shouldBe "initial"
        }

        test("setName updates the underlying DataStore") {
            val dataStore = InMemoryDataStore(samplePreference {})
            val dataSource = NavigoPreferenceDataSource(dataStore)

            dataSource.setName("navigo")

            dataStore.data.first().name shouldBe "navigo"
        }
    })
