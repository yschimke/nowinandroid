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
import androidx.compose.ui.platform.LocalContext
import androidx.wear.tiles.ActionBuilders.AndroidActivity
import androidx.wear.tiles.ActionBuilders.AndroidStringExtra
import androidx.wear.tiles.ActionBuilders.LaunchAction
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.DimensionBuilders.ExpandedDimensionProp
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.LayoutElementBuilders.Box
import androidx.wear.tiles.ModifiersBuilders.Clickable
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.material.Button
import androidx.wear.tiles.material.Colors
import androidx.wear.tiles.material.layouts.MultiButtonLayout
import com.google.android.horologist.compose.previews.WearPreviewDevices
import com.google.android.horologist.compose.tools.TileLayoutPreview
import com.google.android.horologist.tiles.images.drawableResToImageResource
import com.google.android.horologist.tiles.render.SingleTileLayoutRenderer
import com.google.wear.jetfit.compose.theme.AdsColorPalette
import com.google.wear.jetfit.core.compose.R

class OneStepTileRenderer(context: Context) :
    SingleTileLayoutRenderer<OneStepTileRenderer.Data, Unit>(context) {
    override fun createTheme(): Colors {
        return AdsColorPalette.toTileColors()
    }

    data class Data(
        val activities: List<Activity>,
    )

    data class Activity(
        val id: String,
        val title: String,
    )

    override fun renderTile(
        state: Data,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
    ): LayoutElementBuilders.LayoutElement {
        return Box.Builder()
            .setHeight(ExpandedDimensionProp.Builder().build())
            .setWidth(ExpandedDimensionProp.Builder().build())
            .addContent(MultiButtonLayout.Builder()
                .apply {
                    state.activities.take(3).forEach { activity ->
                        addButtonContent(activityLayout(activity))
                    }
                }
                .addButtonContent(openLayout())
                .build())
            .build()
    }

    private fun activityLayout(
        activity: Activity,
    ) = Button.Builder(
        context, Clickable.Builder()
            .setOnClick(
                LaunchAction.Builder()
                    .setAndroidActivity(
                        AndroidActivity.Builder()
                            .setPackageName(context.packageName)
                            .setClassName("com.google.wear.jetfit.wearapp.OneStepActivity")
                            .addKeyToExtraMapping(
                                "activityId", AndroidStringExtra.Builder()
                                    .setValue(activity.id)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    )
        .setContentDescription(activity.title)
        .setImageContent(ActivityIcon)
        .build()

    private fun openLayout() = Button.Builder(
        context, Clickable.Builder()
            .setOnClick(
                LaunchAction.Builder()
                    .setAndroidActivity(
                        AndroidActivity.Builder()
                            .setPackageName(context.packageName)
                            .setClassName("com.google.wear.jetfit.wearapp.OneStepActivity")
                            .build()
                    )
                    .build()
            )
            .build()
    )
        .setContentDescription("OneStep")
        .setImageContent(AppIcon)
        .build()

    override fun Resources.Builder.produceRequestedResources(
        resourceState: Unit,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
        resourceIds: MutableList<String>,
    ) {
        addIdToImageMapping(ActivityIcon, drawableResToImageResource(R.drawable.ic_nordic))
        addIdToImageMapping(AppIcon, drawableResToImageResource(R.drawable.baseline_1x))
    }

    companion object {
        const val ActivityIcon = "activity"
        const val AppIcon = "app"
    }
}

@WearPreviewDevices
@Composable
fun SampleTilePreview() {
    val context = LocalContext.current

    val tileState = remember {
        OneStepTileRenderer.Data(
            listOf(
                OneStepTileRenderer.Activity("1", "Title 1"),
                OneStepTileRenderer.Activity("2", "Title 2"),
                OneStepTileRenderer.Activity("3", "Title 3")
            ),
        )
    }

    val renderer = remember {
        OneStepTileRenderer(context = context)
    }

    TileLayoutPreview(
        tileState,
        Unit,
        renderer
    )
}
