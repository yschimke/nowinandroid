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

package com.google.wear.onestep.data.room

import com.google.wear.onestep.data.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class RoomActivityRepository @Inject constructor(
    private val activityDao: ActivityDao
) : ActivityRepository {
    override suspend fun getCompletedActivitiesInPeriod(
        from: LocalDate,
        to: LocalDate
    ): List<CompletedActivity> {
        val fromInstant = from.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        val toInstant = to.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        return activityDao.getCompletedActivitiesInPeriod(fromInstant, toInstant)
    }

    override fun getActivityById(activityId: String): Flow<CompletedActivity?> {
        return activityDao.getById(activityId)
    }

    override fun getRecentActivities(count: Int): Flow<List<CompletedActivity>> {
        return activityDao.getRecentActivities(count)
    }

    override suspend fun addActivity(completedActivity: CompletedActivity) {
        return activityDao.upsertActivity(completedActivity)
    }
}