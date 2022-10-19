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

package com.google.wear.jetfit.reports

import com.google.wear.jetfit.data.repository.CompletedActivityRepository
import com.google.wear.jetfit.data.repository.CurrentActivityRepository
import com.google.wear.jetfit.data.repository.SettingsRepository
import com.google.wear.jetfit.data.room.CompletedActivity
import com.google.wear.jetfit.proto.Api
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

class WeeklyProgressUseCase
@Inject constructor(
    val currentActivityRepository: CurrentActivityRepository,
    val completedActivityRepository: CompletedActivityRepository,
    val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<WeeklyProgressReport> = flow {
        val today = LocalDate.now()
        val weeklyGoal = settingsRepository.getWeeklyGoal()
        val activities =
            completedActivityRepository.getCompletedActivitiesInPeriod(today.minusWeeks(1), today)

        emitAll(currentActivityRepository.getCurrentActivity().map {
            val allActivities = activities + listOfNotNull(it?.toCompletedActivity())

            WeeklyProgressReport(allActivities, weeklyGoal, "JetFit Weekly")
        })
    }
}

private fun Api.CurrentActivity.toCompletedActivity(): CompletedActivity =
    CompletedActivity(this.activityId, this.title, this.distance, completed = Instant.now())
