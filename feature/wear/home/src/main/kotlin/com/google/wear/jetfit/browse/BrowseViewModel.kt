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

package com.google.wear.jetfit.browse


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wear.jetfit.data.repository.CompletedActivityRepository
import com.google.wear.jetfit.data.room.CompletedActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val completedActivityRepository: CompletedActivityRepository,
) : ViewModel() {
    fun addActivity() {
        viewModelScope.launch {
            completedActivityRepository.addActivity(
                CompletedActivity(
                    System.currentTimeMillis().toString(),
                    "NEW Activity",
                    10.0,
                    Instant.now()
                )
            )
        }
    }

    val state = completedActivityRepository.getRecentActivities(10)
        .map {
            BrowseScreenState(it)
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            BrowseScreenState()
        )

}
