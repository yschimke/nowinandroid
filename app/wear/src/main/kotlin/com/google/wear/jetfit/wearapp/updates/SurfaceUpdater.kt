/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.google.wear.jetfit.wearapp.updates

import com.google.wear.jetfit.complication.ComplicationUpdater
import com.google.wear.jetfit.tile.TileUpdater
import javax.inject.Inject

class SurfaceUpdater @Inject constructor(
    private val complicationUpdater: ComplicationUpdater,
    private val tileUpdater: TileUpdater
) {
    fun onActivityEvent() {
        tileUpdater.updateRecentActivities()
    }

    fun onSensorUpdate() {
        tileUpdater.updateWeeklyGoal()
    }

    fun onDataSync() {
        complicationUpdater.update()
        tileUpdater.updateAll()
    }
}