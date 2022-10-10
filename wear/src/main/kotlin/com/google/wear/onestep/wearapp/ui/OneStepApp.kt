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

@file:OptIn(ExperimentalLifecycleComposeApi::class)

package com.google.wear.onestep.wearapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import com.google.android.horologist.compose.navscaffold.WearNavScaffold
import com.google.android.horologist.compose.navscaffold.scalingLazyColumnComposable
import com.google.wear.onestep.browse.BrowseScreen
import com.google.wear.onestep.login.LoginScreen
import com.google.wear.onestep.wearapp.ui.navigation.Screens

@Composable
fun AdsApp(
    navController: NavHostController,
    viewModel: AppViewModel = hiltViewModel()
) {
    val appState by viewModel.state.collectAsStateWithLifecycle()

    WearNavScaffold(
        startDestination = Screens.Home.route,
        navController = navController,
    ) {
        scalingLazyColumnComposable(
            route = Screens.Home.route,
            scrollStateBuilder = { ScalingLazyListState(initialCenterItemIndex = 0) },
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "onestep://onestep/home"
                }
            )
        ) {
            BrowseScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = hiltViewModel(),
                focusRequester = it.viewModel.focusRequester,
                scrollState = it.scrollableState,
                navController = navController,
                onAuth = { navController.navigate(Screens.Login.route) }
            )
        }

        scalingLazyColumnComposable(
            route = Screens.Activity.route,
            scrollStateBuilder = { ScalingLazyListState(initialCenterItemIndex = 0) },
            arguments = listOf(
                navArgument("activityId") {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "onestep://onestep/activity/{activityId}"
                }
            )
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Activity")
            }
        }

        scalingLazyColumnComposable(
            route = Screens.Login.route,
            scrollStateBuilder = { ScalingLazyListState(initialCenterItemIndex = 0) }
        ) {
            LoginScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = hiltViewModel(),
                focusRequester = it.viewModel.focusRequester,
                scrollState = it.scrollableState,
                navController = navController
            )
        }
    }

    LaunchedEffect(appState.account) {
        viewModel.startLoginFlow {
            navController.navigate(Screens.Login.route)
        }
    }
}
