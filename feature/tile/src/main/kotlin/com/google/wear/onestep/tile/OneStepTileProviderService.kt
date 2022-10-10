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

import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders.Tile
import com.google.android.horologist.tiles.SuspendingTileService
import com.google.wear.onestep.data.repository.ActivityRepository
import com.google.wear.onestep.data.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class OneStepTileProviderService : SuspendingTileService() {
    private var renderer = OneStepTileRenderer(this)

    @Inject
    lateinit var activityRepository: ActivityRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources {
        return renderer.produceRequestedResources(
            resourceState = Unit,
            requestParams = requestParams
        )
    }

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): Tile {
        val today = LocalDate.now()
        val activities =
            activityRepository.getCompletedActivitiesInPeriod(today.minusWeeks(1), today)

        return renderer.renderTimeline(
            OneStepTileRenderer.Data(
            activities.map {
                OneStepTileRenderer.Activity(it.activityId, it.title)
            }
        ), requestParams)
    }
}
