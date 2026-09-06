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
package dev.lscythe.app.navigo.core.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class NavigoDatabaseTest :
    FunSpec({
        test("metadata upsert replaces values and delete removes them") {
            val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            try {
                NavigoDatabase.Schema.create(driver)
                val queries = NavigoDatabase(driver).appMetadataQueries

                queries.upsert("schema", "one")
                queries.selectByKey("schema").executeAsOne() shouldBe "one"

                queries.upsert("schema", "two")
                queries.selectByKey("schema").executeAsOne() shouldBe "two"

                queries.deleteByKey("schema")
                queries.selectByKey("schema").executeAsOneOrNull() shouldBe null
            } finally {
                driver.close()
            }
        }
    })
