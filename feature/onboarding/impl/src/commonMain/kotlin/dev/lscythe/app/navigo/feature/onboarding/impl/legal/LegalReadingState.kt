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
package dev.lscythe.app.navigo.feature.onboarding.impl.legal

internal enum class LegalPrimaryAction {
    ReadToContinue,
    ContinueToTerms,
    ContinueToPrivacy,
    Accept,
}

internal data class LegalReadingState(
    val activeDocument: LegalDocumentType = LegalDocumentType.Terms,
    val termsRead: Boolean = false,
    val privacyRead: Boolean = false,
) {
    val primaryAction: LegalPrimaryAction
        get() =
            when {
                activeDocument == LegalDocumentType.Terms && !termsRead ->
                    LegalPrimaryAction.ReadToContinue
                activeDocument == LegalDocumentType.Privacy && !privacyRead ->
                    LegalPrimaryAction.ReadToContinue
                termsRead && privacyRead -> LegalPrimaryAction.Accept
                termsRead -> LegalPrimaryAction.ContinueToPrivacy
                else -> LegalPrimaryAction.ContinueToTerms
            }

    fun open(type: LegalDocumentType): LegalReadingState = copy(activeDocument = type)

    fun markRead(type: LegalDocumentType): LegalReadingState =
        when (type) {
            LegalDocumentType.Terms -> copy(termsRead = true)
            LegalDocumentType.Privacy -> copy(privacyRead = true)
        }

    fun onPrimaryAction(): LegalReadingState =
        when (primaryAction) {
            LegalPrimaryAction.ContinueToTerms -> open(LegalDocumentType.Terms)
            LegalPrimaryAction.ContinueToPrivacy -> open(LegalDocumentType.Privacy)
            LegalPrimaryAction.ReadToContinue,
            LegalPrimaryAction.Accept -> this
        }
}
