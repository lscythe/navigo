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
package dev.lscythe.app.navigo.core.ui.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import dev.lscythe.app.navigo.core.designsystem.component.atom.NavigoButton
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoModalBottomSheet
import dev.lscythe.app.navigo.core.designsystem.component.molecule.NavigoUnderlinedTextField
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoMaterialKolorThemePreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoPreview
import dev.lscythe.app.navigo.core.designsystem.preview.NavigoThemePreview
import dev.lscythe.app.navigo.core.designsystem.token.NavigoSpacing
import dev.lscythe.app.navigo.core.ui.generated.resources.Res
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_color_picker_apply
import dev.lscythe.app.navigo.core.ui.generated.resources.core_ui_color_picker_hex_label
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

/**
 * Displays a modal HSV color picker with hexadecimal input and an explicit apply action.
 *
 * @param title title displayed in the sheet header.
 * @param selectedColor current color used to initialize the picker.
 * @param onApply called with the current valid color when the user applies it.
 * @param onDismissRequest called when the user dismisses the sheet.
 * @param modifier modifier applied to the modal sheet.
 * @param description optional supporting text displayed below the title.
 * @param sheetState state controlling the sheet; partial expansion should remain disabled so all
 *   controls stay reachable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSelectionBottomSheet(
    title: String,
    selectedColor: Color,
    onApply: (Color) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    sheetState: SheetState =
        rememberBottomSheetState(
            initialValue = SheetValue.Expanded,
            confirmValueChange = { targetValue ->
                targetValue == SheetValue.Expanded
            },
        ),
) {
    val initialHsv = remember(selectedColor) { selectedColor.toHsv() }
    var hue by remember(selectedColor) { mutableFloatStateOf(initialHsv[0]) }
    var saturation by remember(selectedColor) { mutableFloatStateOf(initialHsv[1]) }
    var value by remember(selectedColor) { mutableFloatStateOf(initialHsv[2]) }
    var hexInput by remember(selectedColor) { mutableStateOf(selectedColor.toHex()) }
    var validHex by remember(selectedColor) { mutableStateOf(true) }

    fun select(h: Float = hue, s: Float = saturation, v: Float = value) {
        hue = h.coerceIn(0f, 360f)
        saturation = s.coerceIn(0f, 1f)
        value = v.coerceIn(0f, 1f)
        hexInput = hsvColor(hue, saturation, value).toHex()
        validHex = true
    }

    NavigoModalBottomSheet(
        title = title,
        description = description,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        sheetGesturesEnabled = false,
    ) {
        Column(
            modifier =
                Modifier.verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = NavigoSpacing.screen)
                    .padding(bottom = NavigoSpacing.section),
            verticalArrangement = Arrangement.spacedBy(NavigoSpacing.screen),
        ) {
            SaturationValueField(
                hue = hue,
                saturation = saturation,
                value = value,
                onChange = { s, v -> select(s = s, v = v) },
            )
            HueSlider(hue = hue, onHueChange = { select(h = it) })
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(NavigoSpacing.container),
            ) {
                Box(
                    Modifier.size(56.dp)
                        .background(hsvColor(hue, saturation, value), MaterialTheme.shapes.medium)
                )
                NavigoUnderlinedTextField(
                    value = hexInput,
                    onValueChange = { input ->
                        hexInput = input.uppercase()
                        val parsed = input.parseHexColor()
                        validHex = parsed != null
                        if (parsed != null) {
                            val hsv = parsed.toHsv()
                            hue = hsv[0]
                            saturation = hsv[1]
                            value = hsv[2]
                        }
                    },
                    label = stringResource(Res.string.core_ui_color_picker_hex_label).uppercase(),
                    modifier = Modifier.weight(1f),
                    isError = !validHex,
                )
            }
            NavigoButton(
                onClick = { onApply(hsvColor(hue, saturation, value)) },
                enabled = validHex,
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
            ) {
                Text(stringResource(Res.string.core_ui_color_picker_apply))
            }
        }
    }
}

@Composable
private fun SaturationValueField(
    hue: Float,
    saturation: Float,
    value: Float,
    onChange: (Float, Float) -> Unit,
) {
    val shape = RoundedCornerShape(24.dp)
    Canvas(
        modifier =
            Modifier.fillMaxWidth().aspectRatio(2.2f).background(Color.White, shape).pointerInput(
                hue
            ) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    down.consume()
                    fun update(position: Offset) {
                        onChange(
                            (position.x / size.width).coerceIn(0f, 1f),
                            1f - (position.y / size.height).coerceIn(0f, 1f),
                        )
                    }
                    update(down.position)
                    do {
                        val change = awaitPointerEvent().changes.first()
                        update(change.position)
                        change.consume()
                    } while (change.pressed)
                }
            }
    ) {
        drawRect(Brush.horizontalGradient(listOf(Color.White, hsvColor(hue, 1f, 1f))))
        drawRect(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
        val center = Offset(saturation * size.width, (1f - value) * size.height)
        drawCircle(Color.White, radius = 12.dp.toPx(), center = center)
        drawCircle(
            Color.Black.copy(alpha = 0.35f),
            radius = 12.dp.toPx(),
            center = center,
            style = androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx()),
        )
    }
}

@Composable
private fun HueSlider(hue: Float, onHueChange: (Float) -> Unit) {
    val colors =
        listOf(
            Color.Red,
            Color.Yellow,
            Color.Green,
            Color.Cyan,
            Color.Blue,
            Color.Magenta,
            Color.Red,
        )
    val outlineColor = MaterialTheme.colorScheme.outline
    Canvas(
        modifier =
            Modifier.fillMaxWidth().height(32.dp).pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    down.consume()
                    fun update(x: Float) = onHueChange((x / size.width).coerceIn(0f, 1f) * 360f)
                    update(down.position.x)
                    do {
                        val change = awaitPointerEvent().changes.first()
                        update(change.position.x)
                        change.consume()
                    } while (change.pressed)
                }
            }
    ) {
        val top = size.height / 2f - 7.dp.toPx()
        drawRoundRect(
            brush = Brush.horizontalGradient(colors),
            topLeft = Offset(0f, top),
            size = androidx.compose.ui.geometry.Size(size.width, 14.dp.toPx()),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(7.dp.toPx()),
        )
        val x = hue / 360f * size.width
        drawCircle(Color.White, 14.dp.toPx(), Offset(x, size.height / 2f))
        drawCircle(
            outlineColor,
            14.dp.toPx(),
            Offset(x, size.height / 2f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(3.dp.toPx()),
        )
    }
}

internal fun hsvColor(hue: Float, saturation: Float, value: Float): Color {
    val chroma = value * saturation
    val section = ((hue % 360f) + 360f) % 360f / 60f
    val intermediate = chroma * (1f - abs(section % 2f - 1f))
    val (red, green, blue) =
        when (section.toInt()) {
            0 -> Triple(chroma, intermediate, 0f)
            1 -> Triple(intermediate, chroma, 0f)
            2 -> Triple(0f, chroma, intermediate)
            3 -> Triple(0f, intermediate, chroma)
            4 -> Triple(intermediate, 0f, chroma)
            else -> Triple(chroma, 0f, intermediate)
        }
    val match = value - chroma
    return Color(red + match, green + match, blue + match)
}

internal fun Color.toHsv(): FloatArray {
    val maximum = max(red, max(green, blue))
    val minimum = min(red, min(green, blue))
    val delta = maximum - minimum
    val hue =
        when {
            delta == 0f -> 0f
            maximum == red -> 60f * (((green - blue) / delta) % 6f)
            maximum == green -> 60f * ((blue - red) / delta + 2f)
            else -> 60f * ((red - green) / delta + 4f)
        }
    return floatArrayOf(
        if (hue < 0f) hue + 360f else hue,
        if (maximum == 0f) 0f else delta / maximum,
        maximum,
    )
}

internal fun Color.toHex(): String {
    val rgb =
        ((red * 255).roundToInt() shl 16) or
            ((green * 255).roundToInt() shl 8) or
            (blue * 255).roundToInt()
    return "#" + rgb.toString(16).uppercase().padStart(6, '0')
}

internal fun String.parseHexColor(): Color? {
    val normalized = trim().removePrefix("#")
    if (normalized.length != 6 || normalized.any { it.digitToIntOrNull(16) == null }) return null
    return Color(0xFF000000 or normalized.toLong(16))
}

@OptIn(ExperimentalMaterial3Api::class)
@NavigoThemePreview
@Composable
private fun ColorSelectionBottomSheetPreview() {
    var color by remember { mutableStateOf(Color(0xFF5C8A3E)) }
    NavigoPreview {
        ColorSelectionBottomSheet(
            title = "Pick a colour",
            description = "Choose any colour.",
            selectedColor = color,
            onApply = { color = it },
            onDismissRequest = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@NavigoMaterialKolorThemePreview
@Composable
private fun ColorSelectionBottomSheetMaterialKolorPreview() {
    var color by remember { mutableStateOf(Color(0xFF5C8A3E)) }
    NavigoMaterialKolorPreview {
        ColorSelectionBottomSheet(
            title = "Pick a colour",
            description = "Choose any colour.",
            selectedColor = color,
            onApply = { color = it },
            onDismissRequest = {},
        )
    }
}
