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

package com.google.wear.jetfit.complication

import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import coil.ImageLoader
import com.google.android.horologist.tiles.complication.ComplicationTemplate
import com.google.android.horologist.tiles.complication.DataComplicationService
import com.google.wear.jetfit.data.repository.ActivityRepository
import com.google.wear.jetfit.data.repository.SettingsRepository
import com.google.wear.jetfit.navigation.IntentBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class WeeklyGoalComplicationService() :
    DataComplicationService<Data, ComplicationTemplate<Data>>() {
    @Inject
    lateinit var intentBuilder: IntentBuilder

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var activityRepository: ActivityRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override val renderer: WeeklyGoalTemplate = WeeklyGoalTemplate(this)

    override fun previewData(type: ComplicationType): Data = renderer.previewData()

    override suspend fun data(request: ComplicationRequest): Data {
        val today = LocalDate.now()
        val activities =
            activityRepository.getCompletedActivitiesInPeriod(today.minusWeeks(1), today)
        val weeklyGoal = settingsRepository.getWeeklyGoal()

        return Data(
            "Weekly Activities",
            activities,
            weeklyGoal,
            intentBuilder.buildActivityListIntent()
        )
    }
}
