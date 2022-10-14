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

@file:OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalLifecycleComposeApi::class)

package com.google.wear.jetfit.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import com.google.android.horologist.compose.navscaffold.scrollableColumn

@Suppress("UNUSED_PARAMETER")
@Composable
fun LoginScreen(
    scrollState: ScalingLazyListState,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    navController: NavController,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val signInRequestLauncher = rememberLauncherForActivityResult(
        contract = viewModel.launcherContract
    ) {
        viewModel.updateAccount(it)
    }

    LoginScreen(
        scrollState = scrollState,
        focusRequester = focusRequester,
        modifier = modifier,
        state = state,
        onClick = {
            signInRequestLauncher.launch(Unit)
        },
        logoutClick = {
            viewModel.logout()
        }
    )
}

@Composable
fun LoginScreen(
    scrollState: ScalingLazyListState,
    focusRequester: FocusRequester,
    state: LoginScreenState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    logoutClick: () -> Unit
) {
    ScalingLazyColumn(
        modifier = modifier
            .scrollableColumn(focusRequester, scrollState),
        state = scrollState,
    ) {
        if (state.account != null) {
            item {
                Text("${state.account.displayName}")
            }
            item {
                Text("${state.account.id}")
            }
            item {
                Text("${state.account.grantedScopes.joinToString(", ")}")
            }
            item {
                Button(onClick = logoutClick) {
                    Text("Logout")
                }
            }
        } else {
            item {
                Button(onClick = onClick) {
                    Text("Login")
                }
            }
        }
    }
}
