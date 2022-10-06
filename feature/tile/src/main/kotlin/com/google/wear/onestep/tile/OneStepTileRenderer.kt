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

package com.google.wear.onestep.tile

import android.content.Context
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.DimensionBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.LayoutElementBuilders.Box
import androidx.wear.tiles.LayoutElementBuilders.Column
import androidx.wear.tiles.LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER
import androidx.wear.tiles.LayoutElementBuilders.VERTICAL_ALIGN_CENTER
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.material.Colors
import com.google.android.horologist.tiles.render.SingleTileLayoutRenderer
import com.google.wear.onestep.compose.theme.AdsColorPalette

class OneStepTileRenderer(context: Context) :
    SingleTileLayoutRenderer<Any, Unit>(context) {
    override fun createTheme(): Colors {
        return AdsColorPalette.toTileColors()
    }

    override fun renderTile(
        state: Any,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
    ): LayoutElementBuilders.LayoutElement {
        return Box.Builder()
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .setVerticalAlignment(VERTICAL_ALIGN_CENTER)
            .addContent(
                Column.Builder()
                    .setHorizontalAlignment(HORIZONTAL_ALIGN_CENTER)
                    .setWidth(DimensionBuilders.expand())
                    .apply {

                    }.build()
            ).build()
    }

    override fun Resources.Builder.produceRequestedResources(
        resourceState: Unit,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
        resourceIds: MutableList<String>,
    ) {
    }
}
