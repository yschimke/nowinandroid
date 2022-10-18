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
import androidx.wear.tiles.material.layouts.MultiButtonLayout
import com.google.android.horologist.compose.previews.WearPreviewDevices
import com.google.android.horologist.compose.tools.TileLayoutPreview
import com.google.android.horologist.tiles.images.drawableResToImageResource
import com.google.android.horologist.tiles.render.SingleTileLayoutRenderer
import com.google.wear.jetfit.core.compose.R
import com.google.wear.jetfit.data.room.CompletedActivity
import com.google.wear.jetfit.reports.SampleData
import com.google.wear.jetfit.reports.WeeklyProgressReport

class RecentActivitiesTileRenderer(context: Context) : SingleTileLayoutRenderer<WeeklyProgressReport, Unit>(context) {

    override fun renderTile(
        state: WeeklyProgressReport,
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
        activity: CompletedActivity,
    ) = Button.Builder(
        context, Clickable.Builder()
            .setOnClick(
                LaunchAction.Builder()
                    .setAndroidActivity(
                        AndroidActivity.Builder()
                            .setPackageName(context.packageName)
                            .setClassName("com.google.wear.jetfit.wearapp.JetFitActivity")
                            .addKeyToExtraMapping(
                                "activityId", AndroidStringExtra.Builder()
                                    .setValue(activity.activityId)
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
                            .setClassName("com.google.wear.jetfit.wearapp.JetFitActivity")
                            .build()
                    )
                    .build()
            )
            .build()
    )
        .setContentDescription("JetFit")
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
fun RecentActivitiesTilePreview() {
    val context = LocalContext.current

    val tileState = remember {
        SampleData.Weekly
    }

    val renderer = remember {
        RecentActivitiesTileRenderer(context = context)
    }

    TileLayoutPreview(
        tileState,
        Unit,
        renderer
    )
}
