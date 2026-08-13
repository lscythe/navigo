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
package dev.lscythe.app.navigo.core.database.di

import android.app.Application
import androidx.room3.Room
import dev.lscythe.app.navigo.core.database.NavigoDatabase
import dev.lscythe.app.navigo.core.database.dao.SampleDao
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
@BindingContainer
internal object DatabaseBindings {

    @Provides
    @SingleIn(AppScope::class)
    fun provideNavigoDatabase(application: Application): NavigoDatabase =
        Room.databaseBuilder(application, NavigoDatabase::class.java, "navigo.db").build()

    @Provides fun provideSampleDao(database: NavigoDatabase): SampleDao = database.sampleDao()
}
