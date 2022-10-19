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

import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.DimensionBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.ModifiersBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.ResourceBuilders.AndroidImageResourceByResId
import androidx.wear.tiles.ResourceBuilders.ImageResource
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import androidx.wear.tiles.TimelineBuilders
import androidx.wear.tiles.material.Button
import androidx.wear.tiles.material.layouts.MultiButtonLayout
import com.google.android.horologist.tiles.render.PERMANENT_RESOURCES_VERSION
import com.google.common.util.concurrent.ListenableFuture
import com.google.wear.jetfit.core.compose.R
import com.google.wear.jetfit.data.repository.ActivityRepository
import com.google.wear.jetfit.data.room.CompletedActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.guava.future
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class StandardRecentActivitiesTileProviderService : TileService() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    @Inject
    lateinit var activityRepository: ActivityRepository

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<Tile> {
        val today = LocalDate.now()

        return serviceScope.future {
            val activities =
                activityRepository.getCompletedActivitiesInPeriod(today.minusWeeks(1), today)

            val rootLayout = LayoutElementBuilders.Box.Builder()
                .setHeight(DimensionBuilders.ExpandedDimensionProp.Builder().build())
                .setWidth(DimensionBuilders.ExpandedDimensionProp.Builder().build())
                .addContent(
                    MultiButtonLayout.Builder()
                        .apply {
                            activities.take(3).forEach { activity ->
                                addButtonContent(activityLayout(activity))
                            }
                        }
                        .addButtonContent(openLayout())
                        .build())
                .build()

            val singleTileTimeline = TimelineBuilders.Timeline.Builder()
                .addTimelineEntry(
                    TimelineBuilders.TimelineEntry.Builder()
                        .setLayout(
                            LayoutElementBuilders.Layout.Builder()
                                .setRoot(rootLayout)
                                .build()
                        )
                        .build()
                )
                .build()

            Tile.Builder()
                .setResourcesVersion(PERMANENT_RESOURCES_VERSION)
                .setTimeline(singleTileTimeline)
                .setFreshnessIntervalMillis(20_000)
                .build()
        }
    }

    private fun activityLayout(
        activity: CompletedActivity,
    ) = Button.Builder(
        this, ModifiersBuilders.Clickable.Builder()
            .setOnClick(
                ActionBuilders.LaunchAction.Builder()
                    .setAndroidActivity(
                        ActionBuilders.AndroidActivity.Builder()
                            .setPackageName(packageName)
                            .setClassName("com.google.wear.jetfit.wearapp.JetFitActivity")
                            .addKeyToExtraMapping(
                                "activityId", ActionBuilders.AndroidStringExtra.Builder()
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
        .setImageContent(RecentActivitiesTileRenderer.ActivityIcon)
        .build()

    private fun openLayout() = Button.Builder(
        this, ModifiersBuilders.Clickable.Builder()
            .setOnClick(
                ActionBuilders.LaunchAction.Builder()
                    .setAndroidActivity(
                        ActionBuilders.AndroidActivity.Builder()
                            .setPackageName(packageName)
                            .setClassName("com.google.wear.jetfit.wearapp.JetFitActivity")
                            .build()
                    )
                    .build()
            )
            .build()
    )
        .setContentDescription("JetFit")
        .setImageContent(RecentActivitiesTileRenderer.AppIcon)
        .build()

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> {
        return serviceScope.future {
            ResourceBuilders.Resources.Builder()
                .setVersion(requestParams.version)
                .apply {
                    addIdToImageMapping(
                        RecentActivitiesTileRenderer.ActivityIcon, ImageResource.Builder()
                            .setAndroidResourceByResId(
                                AndroidImageResourceByResId.Builder()
                                    .setResourceId(R.drawable.ic_nordic)
                                    .build()
                            )
                            .build()
                    )
                    addIdToImageMapping(
                        RecentActivitiesTileRenderer.AppIcon, ImageResource.Builder()
                            .setAndroidResourceByResId(
                                AndroidImageResourceByResId.Builder()
                                    .setResourceId(R.drawable.baseline_1x)
                                    .build()
                            )
                            .build()
                    )
                }
                .build()

        }
    }
}
