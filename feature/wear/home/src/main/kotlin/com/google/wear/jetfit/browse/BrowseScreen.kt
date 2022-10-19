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

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipColors
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import com.google.android.horologist.compose.layout.StateUtils.rememberStateWithLifecycle
import com.google.android.horologist.compose.navscaffold.scrollableColumn
import com.google.android.horologist.compose.previews.WearPreviewDevices
import com.google.wear.jetfit.data.room.CompletedActivity
import com.google.wear.jetfit.navigation.JetFitNavController.navigateToActivity
import com.google.wear.jetfit.proto.Api.CurrentActivity
import com.google.wear.jetfit.proto.currentActivity
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
        onStartActivity = {
            viewModel.startActivity()
        },
        onCompleteActivity = {
            viewModel.completeActivity(it)
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
    onStartActivity: () -> Unit,
    onCompleteActivity: (CurrentActivity) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScalingLazyColumn(
        modifier = modifier
            .scrollableColumn(focusRequester, scrollState),
        state = scrollState,
    ) {
        item {
            ListHeader() {
                Text(text = "Current Activity")
            }
        }
        if (state.current != null) {
            item {
                Chip(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onCompleteActivity(state.current) },
                    label = {
                        Text("Complete " + state.current.title)
                    })
            }
        }
        if (state.current == null) {
            item {
                Chip(modifier = Modifier.fillMaxWidth(), onClick = onStartActivity, label = {
                    Text("Start")
                })
            }
        }
        item {
            Spacer(Modifier.size(10.dp))
        }
        item {
            ListHeader() {
                Text(text = "Activities")
            }
        }
        items(state.activities) {
            Chip(modifier = Modifier.fillMaxWidth(), onClick = {
                onClick(it)
            }, label = {
                Text(it.title)
            }, colors = ChipDefaults.secondaryChipColors())
        }
        if (state.activities.isEmpty()) {
            item {
                Text(text = "No Activities")
            }
        }
        item {
            Spacer(Modifier.size(10.dp))
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
fun BrowseScreenInactivePreview() {
    BrowseScreen(
        scrollState = rememberScalingLazyListState(),
        focusRequester = remember { FocusRequester() },
        state = BrowseScreenState(
            null,
            listOf(
                CompletedActivity("1", "Running", 15.0, Instant.now()),
                CompletedActivity("2", "Walking", 20.0, Instant.now())
            )
        ),
        onClick = {},
        onAuth = null,
        onCompleteActivity = {},
        onStartActivity = {}
    )
}

@WearPreviewDevices
@Composable
fun BrowseScreenActivePreview() {
    BrowseScreen(
        scrollState = rememberScalingLazyListState(),
        focusRequester = remember { FocusRequester() },
        state = BrowseScreenState(
            currentActivity {
                activityId = "1"
                title = "Run"
                distance = 10.0
                active = true
            },
            listOf(
                CompletedActivity("1", "Running", 15.0, Instant.now()),
                CompletedActivity("2", "Walking", 20.0, Instant.now())
            )
        ),
        onClick = {},
        onAuth = null,
        onCompleteActivity = {},
        onStartActivity = {}
    )
}
