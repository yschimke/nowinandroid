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

package com.google.wear.jetfit.wearapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.wear.jetfit.BuildConfig
import com.google.wear.jetfit.wearapp.ui.JetFitApp
import dagger.hilt.android.AndroidEntryPoint
import com.google.wear.jetfit.compose.util.JankPrinter

@AndroidEntryPoint
class JetFitActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jankPrinter: JankPrinter? = if (BuildConfig.DEBUG) null else JankPrinter()

        setContent {
            navController = rememberSwipeDismissableNavController()

            JetFitApp(navController = navController)

            if (jankPrinter != null) {
                LaunchedEffect(Unit) {
                    navController.currentBackStackEntryFlow.collect {
                        jankPrinter.setRouteState(route = it.destination.route)
                    }
                }
            }
        }

        jankPrinter?.installJankStats(activity = this)
    }
}
