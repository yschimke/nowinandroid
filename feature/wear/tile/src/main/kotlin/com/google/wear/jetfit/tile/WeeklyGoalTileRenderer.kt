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

package com.google.wear.jetfit.tile

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.wear.tiles.ColorBuilders.argb
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.DimensionBuilders.DpProp
import androidx.wear.tiles.DimensionBuilders.ExpandedDimensionProp
import androidx.wear.tiles.DimensionBuilders.WrappedDimensionProp
import androidx.wear.tiles.LayoutElementBuilders.Column
import androidx.wear.tiles.LayoutElementBuilders.Image
import androidx.wear.tiles.LayoutElementBuilders.LayoutElement
import androidx.wear.tiles.ModifiersBuilders.Clickable
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.material.ChipColors
import androidx.wear.tiles.material.CompactChip
import androidx.wear.tiles.material.Text
import androidx.wear.tiles.material.Typography.TYPOGRAPHY_CAPTION1
import androidx.wear.tiles.material.layouts.PrimaryLayout
import com.google.android.horologist.compose.previews.WearPreviewDevices
import com.google.android.horologist.compose.previews.WearPreviewFontSizes
import com.google.android.horologist.compose.tools.TileLayoutPreview
import com.google.android.horologist.tiles.canvas.canvasToImageResource
import com.google.android.horologist.tiles.render.SingleTileLayoutRenderer
import com.google.wear.jetfit.charts.barPaint
import com.google.wear.jetfit.charts.goalChart
import com.google.wear.jetfit.charts.textPaint
import com.google.wear.jetfit.reports.SampleData
import com.google.wear.jetfit.reports.WeeklyProgressReport

class WeeklyGoalTileRenderer(context: Context) :
    SingleTileLayoutRenderer<WeeklyProgressReport, WeeklyProgressReport>(context) {

    override fun renderTile(
        state: WeeklyProgressReport,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
    ): LayoutElement {
        return PrimaryLayout.Builder(deviceParameters)
            .setContent(
                Column.Builder()
                    .addContent(
                        Image.Builder()
                            .setResourceId(DailyActivities)
                            .setWidth(DpProp.Builder().setValue(160f).build())
                            .setHeight(DpProp.Builder().setValue(80f).build())
                            .build()
                    )
                    .setWidth(ExpandedDimensionProp.Builder().build())
                    .setHeight(WrappedDimensionProp.Builder().build())
                    .build()
            )
            .setPrimaryLabelTextContent(
                Text.Builder(context, state.summary)
                    .setTypography(TYPOGRAPHY_CAPTION1)
                    .setColor(argb(theme.onSurface))
                    .build()
            )
            .setPrimaryChipContent(
                CompactChip.Builder(
                    context,
                    "Open JetFit",
                    Clickable.Builder()
                        .setOnClick(openActivityClickable(context))
                        .build(), deviceParameters
                )
                    .setChipColors(ChipColors.primaryChipColors(theme))
                    .build()
            )
            .build()
    }

    override fun Resources.Builder.produceRequestedResources(
        resourceState: WeeklyProgressReport,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
        resourceIds: MutableList<String>,
    ) {
        val density = Density(context)
        addIdToImageMapping(
            DailyActivities,
            canvasToImageResource(Size(160f * density.density, 100f *  density.density), density) {
                goalChart(resourceState)
            })
    }

    companion object {
        const val DailyActivities = "daily_activities"
    }
}

@WearPreviewDevices
@WearPreviewFontSizes
@Composable
fun WeeklyGoalTilePreview() {
    val context = LocalContext.current

    val renderer = remember { WeeklyGoalTileRenderer(context = context) }

    TileLayoutPreview(
        state = SampleData.Weekly,
        resourceState = SampleData.Weekly,
        renderer = renderer
    )
}
