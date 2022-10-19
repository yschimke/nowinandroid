/*
 * Copyright 2021 Google LLC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.wear.jetfit.data.hilt

import android.content.Context
import androidx.room.Room
import com.google.wear.jetfit.data.datastore.DataStoreCurrentActivityRepository
import com.google.wear.jetfit.data.datastore.DataStoreSettingsRepository
import com.google.wear.jetfit.data.repository.CompletedActivityRepository
import com.google.wear.jetfit.data.repository.CurrentActivityRepository
import com.google.wear.jetfit.data.repository.SettingsRepository
import com.google.wear.jetfit.data.room.ActivityDao
import com.google.wear.jetfit.data.room.ActivityDatabase
import com.google.wear.jetfit.data.room.RoomCompletedActivityRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Module
    @InstallIn(SingletonComponent::class)
    interface Declarations {
        @Binds
        fun bindsRoomActivityRepository(
            repository: RoomCompletedActivityRepository
        ): CompletedActivityRepository

        @Binds
        fun bindsDataStoreSettingsRepository(
            repository: DataStoreSettingsRepository
        ): SettingsRepository

        @Binds
        fun bindsDataStoreCurrentActivityRepository(
            repository: DataStoreCurrentActivityRepository
        ): CurrentActivityRepository
    }

    private val DATABASE_NAME = "activities"

    @Provides
    @Singleton
    fun activitiesDatabase(
        @ApplicationContext context: Context
    ): ActivityDatabase {
        return Room.databaseBuilder(
            context,
            ActivityDatabase::class.java,
            DATABASE_NAME
        )
            // Until stable, don't require incrementing MediaDatabase version.
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun activityDao(
        database: ActivityDatabase
    ): ActivityDao = database.activityDao()
}
