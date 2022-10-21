/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.wear.jetfit.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

val JetFitColorPalette = Colors(
    primary = Color(0xFF63D7C5L),
    primaryVariant = Color(0xff668888L),
    onPrimary = Color.Black,
    secondary = Color(0xfffffac0L),
    secondaryVariant = Color(0xff240047L),
    onSecondary = Color(0xFF0060bfL),
    onSurfaceVariant = Color(0xFF63D7C5L),
    onSurface = Color.White
)

@Composable
fun JetFitTheme(
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = JetFitColorPalette,
        content = content
    )
}