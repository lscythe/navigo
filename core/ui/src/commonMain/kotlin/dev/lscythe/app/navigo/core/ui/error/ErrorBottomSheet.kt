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
package dev.lscythe.app.navigo.core.ui.error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoTextButton
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoModalBottomSheet
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing

/** Identifies the contextual meaning of an error sheet's primary action. */
enum class ErrorPrimaryAction {
    Retry,
    Continue,
}

/**
 * Displays an error with caller-owned illustration and actions.
 *
 * @param title the error heading
 * @param message supporting text explaining the error or recovery
 * @param illustration centered visual content describing the error
 * @param primaryActionLabel localized label for the primary action
 * @param onPrimaryAction called when the primary action is selected
 * @param onCancel called when the cancel action is selected
 * @param cancelLabel localized label for the cancel action
 * @param modifier the [Modifier] to apply to this sheet
 * @param primaryAction semantic meaning of the primary action
 * @param onDismissRequest called when the user dismisses the sheet
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NavigoErrorBottomSheet(
    title: String,
    message: String,
    illustration: @Composable BoxScope.() -> Unit,
    primaryActionLabel: String,
    onPrimaryAction: () -> Unit,
    onCancel: () -> Unit,
    cancelLabel: String,
    modifier: Modifier = Modifier,
    primaryAction: ErrorPrimaryAction = ErrorPrimaryAction.Retry,
    onDismissRequest: () -> Unit = onCancel,
) {
    NavigoModalBottomSheet(
        title = title,
        description = message,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Column(
            modifier =
                Modifier.fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = NavigoSpacing.screen)
                    .padding(bottom = NavigoSpacing.screen),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .heightIn(min = 160.dp, max = 200.dp)
                        .testTag("error-illustration"),
                contentAlignment = Alignment.Center,
                content = illustration,
            )
            NavigoButton(
                onClick = onPrimaryAction,
                modifier =
                    Modifier.fillMaxWidth()
                        .heightIn(min = 54.dp)
                        .testTag(
                            when (primaryAction) {
                                ErrorPrimaryAction.Retry -> "error-primary-retry"
                                ErrorPrimaryAction.Continue -> "error-primary-continue"
                            }
                        )
                        .semantics { stateDescription = primaryAction.name },
            ) {
                Text(primaryActionLabel)
            }
            NavigoTextButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            ) {
                Text(cancelLabel)
            }
        }
    }
}

@Composable
internal fun ErrorBottomSheetPreviewContent(primaryAction: ErrorPrimaryAction) {
    NavigoErrorBottomSheet(
        title =
            if (primaryAction == ErrorPrimaryAction.Retry) "Connection lost"
            else "Live data unavailable",
        message =
            if (primaryAction == ErrorPrimaryAction.Retry) "Check your connection and try again."
            else "You can continue with saved data.",
        illustration = {
            Box(
                Modifier.heightIn(min = 120.dp, max = 160.dp)
                    .fillMaxWidth(.5f)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
            )
        },
        primaryActionLabel = if (primaryAction == ErrorPrimaryAction.Retry) "Retry" else "Continue",
        onPrimaryAction = {},
        onCancel = {},
        cancelLabel = "Cancel",
        primaryAction = primaryAction,
    )
}
