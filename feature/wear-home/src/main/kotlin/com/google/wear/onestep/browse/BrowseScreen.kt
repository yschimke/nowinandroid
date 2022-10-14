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

package com.google.wear.onestep.browse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import com.google.android.horologist.compose.layout.StateUtils.rememberStateWithLifecycle
import com.google.android.horologist.compose.navscaffold.scrollableColumn
import com.google.android.horologist.compose.previews.WearPreviewDevices
import com.google.wear.onestep.data.room.CompletedActivity
import com.google.wear.onestep.navigation.OneStepNavController.navigateToActivity
import com.google.wear.onestep.navigation.Screens
import java.time.Instant

@Composable
fun BrowseScreen(
    scrollState: ScalingLazyListState,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    viewModel: BrowseViewModel = hiltViewModel(),
    navController: NavController,
    onAuth: (() -> Unit)?
) {
    val state by rememberStateWithLifecycle(viewModel.state)

    BrowseScreen(
        scrollState = scrollState,
        focusRequester = focusRequester,
        modifier = modifier,
        state = state,
        onClick = {
            navController.navigateToActivity(it.activityId)
        },
        onAuth = onAuth,
        onAddActivity = {
            viewModel.addActivity()
        }
    )
}

@Composable
fun BrowseScreen(
    scrollState: ScalingLazyListState,
    focusRequester: FocusRequester,
    state: BrowseScreenState,
    onClick: (CompletedActivity) -> Unit,
    onAuth: (() -> Unit)?,
    onAddActivity: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScalingLazyColumn(
        modifier = modifier
            .scrollableColumn(focusRequester, scrollState),
        state = scrollState,
    ) {
        items(state.activities) {
            Chip(onClick = {
                onClick(it)
            }, label = {
                Text(it.title)
            })
        }
        item {
            Chip(onClick = onAddActivity, label = {
                Text("Add Activity")
            })
        }
        if (onAuth != null) {
            item {
                Chip(onClick = onAuth, label = {
                    Text("Account")
                })
            }
        }
    }
}

@WearPreviewDevices
@Composable
fun BrowseScreenPreview() {
    BrowseScreen(
        scrollState = rememberScalingLazyListState(),
        focusRequester = remember { FocusRequester() },
        state = BrowseScreenState(
            listOf(
                CompletedActivity("1", "Running", 15.0, Instant.now()),
                CompletedActivity("2", "Walking", 20.0, Instant.now())
            )
        ),
        onClick = {},
        onAuth = null,
        onAddActivity = {}
    )
}
