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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.wear.onestep.data.room.CompletedActivity
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDate

/**
 * DAO for [CompletedActivity].
 */
@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(mediaList: List<CompletedActivity>)

    @Query(
        value = """
        DELETE FROM CompletedActivity
        WHERE activityId in (:activityIds)
    """
    )
    public suspend fun delete(activityIds: List<String>)

    @Query("SELECT * FROM CompletedActivity WHERE completed > :from AND completed < :to")
    suspend fun getCompletedActivitiesInPeriod(
        from: Instant,
        to: Instant
    ): List<CompletedActivity>

    @Query("SELECT * FROM CompletedActivity WHERE activityId = :activityId")
    fun getById(activityId: String): Flow<CompletedActivity?>

    @Query("SELECT * FROM CompletedActivity ORDER BY completed desc LIMIT :count")
    fun getRecentActivities(count: Int): Flow<List<CompletedActivity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertActivity(completedActivity: CompletedActivity)
}
