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

package com.google.wear.jetfit.wearapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.wear.compose.material.ScalingLazyListState
import com.google.android.horologist.compose.navscaffold.WearNavScaffold
import com.google.android.horologist.compose.navscaffold.scalingLazyColumnComposable
import com.google.wear.jetfit.activity.ActivityScreen
import com.google.wear.jetfit.browse.BrowseScreen
import com.google.wear.jetfit.compose.theme.JetFitTheme
import com.google.wear.jetfit.login.LoginScreen
import com.google.wear.jetfit.navigation.Screens

@Composable
fun JetFitApp(
    navController: NavHostController,
) {
    JetFitTheme {
        WearNavScaffold(
            startDestination = Screens.Home.route,
            navController = navController,
        ) {
            scalingLazyColumnComposable(
                route = Screens.Home.route,
                scrollStateBuilder = { ScalingLazyListState(initialCenterItemIndex = 0) },
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "jetfit://jetfit/home"
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
                        uriPattern = "jetfit://jetfit/activity/{activityId}"
                    }
                )
            ) {
                ActivityScreen(
                    scrollState = it.scrollableState,
                    focusRequester = it.viewModel.focusRequester
                )
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
    }
}
