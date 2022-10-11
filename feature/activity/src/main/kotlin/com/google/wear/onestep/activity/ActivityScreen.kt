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

package com.google.wear.onestep.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberScalingLazyListState
import com.google.android.horologist.compose.layout.StateUtils.rememberStateWithLifecycle
import com.google.android.horologist.compose.navscaffold.scrollableColumn
import com.google.android.horologist.compose.previews.WearPreviewDevices
import com.google.wear.onestep.data.room.CompletedActivity
import java.time.Instant
import java.time.ZoneId

@Composable
fun ActivityScreen(
    scrollState: ScalingLazyListState,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    viewModel: ActivityViewModel = hiltViewModel(),
) {
    val state by rememberStateWithLifecycle(viewModel.state)

    ActivityScreen(
        scrollState = scrollState,
        focusRequester = focusRequester,
        modifier = modifier,
        state = state,
    )
}

@Composable
fun ActivityScreen(
    scrollState: ScalingLazyListState,
    focusRequester: FocusRequester,
    state: ActivityScreenState,
    modifier: Modifier = Modifier,
) {
    ScalingLazyColumn(
        modifier = modifier
            .scrollableColumn(focusRequester, scrollState),
        state = scrollState,
    ) {
        if (state.activity != null) {
            item {
                Text(text = state.activity.title, style = MaterialTheme.typography.title1)
            }
            item {
                val dateTime = remember(state.activity.completed) {
                    state.activity.completed.atZone(ZoneId.systemDefault())
                }
                Text(
                    text = "Completed " + dateTime.toLocalDate(),
                    style = MaterialTheme.typography.title1
                )
            }
            item {
                Text(
                    text = "Distance " + state.activity.distance,
                    style = MaterialTheme.typography.title1
                )
            }
        }
    }
}

@WearPreviewDevices
@Composable
fun ActivityScreenPreview() {
    ActivityScreen(
        rememberScalingLazyListState(), remember { FocusRequester() }, ActivityScreenState(
            CompletedActivity("1", "Running", 30.0, Instant.now())
        )
    )
}
