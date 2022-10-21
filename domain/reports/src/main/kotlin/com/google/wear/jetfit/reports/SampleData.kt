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
import java.time.LocalDate
import java.time.ZoneOffset

object SampleData {
    val Today = LocalDate.of(2022, 7, 9)

    val Weekly = WeeklyProgressReport(
        listOf(
            CompletedActivity("1", "Title 1", 10.0, Today.atStartOfDay().toInstant(ZoneOffset.UTC)),
            CompletedActivity(
                "2",
                "Title 2",
                20.0,
                Today.minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
            CompletedActivity(
                "3",
                "Title 3",
                5.0,
                Today.minusDays(2).atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
            CompletedActivity(
                "4",
                "Title 4",
                5.0,
                Today.minusDays(3).atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
            CompletedActivity(
                "5",
                "Title 5",
                5.0,
                Today.minusDays(4).atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
            CompletedActivity(
                "6",
                "Title 6",
                5.0,
                Today.minusDays(5).atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
            CompletedActivity(
                "7",
                "Title 7",
                5.0,
                Today.minusDays(5).atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
            CompletedActivity(
                "8",
                "Title 8",
                5.0,
                Today.minusDays(6).atStartOfDay().toInstant(ZoneOffset.UTC)
            )
        ),
        weeklyGoal = 50.0,
        title = "JetFit"
    )
}