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
package dev.lscythe.app.navigo.domain.legal.repository

import dev.lscythe.app.navigo.domain.legal.model.LegalDocumentSet
import dev.lscythe.app.navigo.domain.settings.model.AppLanguage

sealed interface LegalFailure {
    val message: String?

    data class Network(override val message: String? = null) : LegalFailure

    data class Server(override val message: String? = null) : LegalFailure

    data class InvalidContent(override val message: String? = null) : LegalFailure

    data class CacheInconsistency(override val message: String? = null) : LegalFailure

    data class Unknown(override val message: String? = null) : LegalFailure
}

sealed interface LegalResult<out T> {
    data class Success<T>(val value: T) : LegalResult<T>

    data class Failure(val failure: LegalFailure) : LegalResult<Nothing>
}

interface LegalRepository {
    suspend fun getDocuments(language: AppLanguage): LegalResult<LegalDocumentSet>
}
