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

package com.google.wear.jetfit.activity


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.wear.jetfit.data.repository.CompletedActivityRepository
import com.google.wear.jetfit.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    completedActivityRepository: CompletedActivityRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val activityId: String = savedStateHandle[Screens.Activity.activityIdArg]!!

    val state = completedActivityRepository.getActivityById(activityId)
        .map {
            ActivityScreenState(it)
        }
        .stateIn(
        viewModelScope, started = SharingStarted.WhileSubscribed(5000), ActivityScreenState()
    )
}
