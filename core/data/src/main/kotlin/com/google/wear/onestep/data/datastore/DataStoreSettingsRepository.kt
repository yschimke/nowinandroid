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

package com.google.wear.onestep.data.datastore

import android.content.Context
import com.google.wear.onestep.data.repository.ActivityRepository
import com.google.wear.onestep.data.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class DataStoreSettingsRepository @Inject constructor(
    private @ApplicationContext val application: Context
) : SettingsRepository {
    val dataStore = application.settingsStore

    override suspend fun getWeeklyGoal(): Double {
        return dataStore.data.first().weeklyGoal
    }
}