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

package com.google.wear.jetfit.weeklygoal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.horologist.compose.layout.StateUtils.rememberStateWithLifecycle
import com.google.android.horologist.compose.layout.fillMaxRectangle
import com.google.android.horologist.compose.previews.WearPreviewDevices
import com.google.wear.jetfit.charts.GoalChart
import com.google.wear.jetfit.reports.SampleData

@Composable
fun WeeklyGoalScreen(
    modifier: Modifier = Modifier,
    viewModel: WeeklyGoalViewModel = hiltViewModel(),
) {
    val state by rememberStateWithLifecycle(viewModel.state)

    WeeklyGoalScreen(
        modifier = modifier,
        state = state,
    )
}

@Composable
fun WeeklyGoalScreen(
    state: WeeklyGoalScreenState,
    modifier: Modifier = Modifier,
) {
    if (state.weeklyGoalReport != null) {
        GoalChart(modifier = modifier.fillMaxRectangle(), state = state.weeklyGoalReport)
    }
}

@WearPreviewDevices
@Composable
fun WeeklyGoalScreenPreview() {
    WeeklyGoalScreen(
        WeeklyGoalScreenState(
            SampleData.Weekly
        )
    )
}
