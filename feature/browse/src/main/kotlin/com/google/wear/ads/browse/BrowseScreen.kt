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

package com.google.wear.ads.browse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.navigation.NavController
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import com.google.android.horologist.compose.layout.StateUtils.rememberStateWithLifecycle
import com.google.android.horologist.compose.navscaffold.scrollableColumn

@Suppress("UNUSED_PARAMETER")
@Composable
fun BrowseScreen(
    scrollState: ScalingLazyListState,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    viewModel: BrowseViewModel,
    navController: NavController,
    onAuth: (() -> Unit)?
) {
    val state by rememberStateWithLifecycle(viewModel.state)

    BrowseScreen(
        scrollState = scrollState,
        focusRequester = focusRequester,
        modifier = modifier,
        state = state,
        onAuth = onAuth,
    )
}

@Composable
fun BrowseScreen(
    scrollState: ScalingLazyListState,
    focusRequester: FocusRequester,
    state: BrowseScreenState,
    modifier: Modifier = Modifier,
    onAuth: (() -> Unit)?
) {
    ScalingLazyColumn(
        modifier = modifier
            .scrollableColumn(focusRequester, scrollState),
        state = scrollState,
    ) {
        if (onAuth != null) {
            item {
                Chip(onClick = onAuth, label = {
                    Text("Auth")
                })
            }
        }
    }
}
