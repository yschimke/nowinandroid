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

package com.google.wear.jetfit.reports

import com.google.wear.jetfit.data.room.CompletedActivity
import java.time.DayOfWeek
import java.time.ZoneId

data class WeeklyProgressReport(
    val activities: List<CompletedActivity>,
    val weeklyGoal: Double,
    val title: String
) {
    val dailyTotals: Map<DayOfWeek, Double>
        get() = activities.groupBy { it.completed.atZone(ZoneId.systemDefault()).dayOfWeek }
            .mapValues { (k, v) -> v.sumOf { it.distance } }

    val totalActivityDistance: Double = activities.sumOf { it.distance }
    val percentString: String = "${(totalActivityDistance / weeklyGoal * 100f).toInt()}%"
    val summary: String = "${activities.size} runs / ${totalActivityDistance.toInt()} miles"
}