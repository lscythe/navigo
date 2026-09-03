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

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import java.io.File

private const val DATABASE_NAME = "navigo.db"

@ContributesTo(AppScope::class)
@BindingContainer
object DatabaseDriverBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun provideDatabaseDriver(): SqlDriver {
        val databaseFile = File(DATABASE_NAME)
        return JdbcSqliteDriver("jdbc:sqlite:$DATABASE_NAME").also { driver ->
            if (!databaseFile.exists()) NavigoDatabase.Schema.create(driver)
        }
    }
}
