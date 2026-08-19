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

import androidx.lifecycle.ViewModel
import dev.lscythe.app.navigo.util.ProfileVerifierLogger
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import kotlin.reflect.KClass

@DependencyGraph(AppScope::class)
interface NavigoGraph : MetroAppComponentProviders {
    val profileVerifierLogger: ProfileVerifierLogger

    @Multibinds(allowEmpty = true)
    val assistedViewModelFactories: Map<KClass<out ViewModel>, () -> ViewModelAssistedFactory>

    @Multibinds(allowEmpty = true)
    val manualAssistedViewModelFactories:
        Map<KClass<out ManualViewModelAssistedFactory>, () -> ManualViewModelAssistedFactory>
}
