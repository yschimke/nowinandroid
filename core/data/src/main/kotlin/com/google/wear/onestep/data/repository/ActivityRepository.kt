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

package com.google.wear.onestep.data.repository

import com.google.wear.onestep.data.room.CompletedActivity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ActivityRepository {
    suspend fun getCompletedActivitiesInPeriod(
        from: LocalDate,
        to: LocalDate
    ): List<CompletedActivity>

    fun getActivityById(activityId: String): Flow<CompletedActivity?>
    fun getRecentActivities(count: Int): Flow<List<CompletedActivity>>
    suspend fun addActivity(completedActivity: CompletedActivity)
}