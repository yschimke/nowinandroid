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

package com.google.wear.jetfit.navigation

sealed class Screens(val route: String) {
    object Home : Screens("home")

    object Login : Screens("login")

    object Activity : Screens("activity/{activityId}") {
        fun routeFor(activityId: String): String {
            return "activity/${activityId}"
        }

        val activityIdArg: String = "activityId"
    }
}
