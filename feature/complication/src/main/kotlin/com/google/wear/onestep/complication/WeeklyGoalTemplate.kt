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

package com.google.wear.onestep.complication;

import android.app.PendingIntent
import android.content.Context
import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import com.google.android.horologist.tiles.complication.TypedComplicationTemplate
import com.google.wear.onestep.core.compose.R
import com.google.wear.onestep.room.CompletedActivity

public class WeeklyGoalTemplate(
    context: Context
) :
    TypedComplicationTemplate<WeeklyGoalTemplate.Data>(context) {
    data class Data(
        val title: String,
        val activities: List<CompletedActivity>,
        val goal: Double,
        val launchIntent: PendingIntent?
    )

    override fun previewData(): Data = Data(
        title = "OneStep",
        activities = listOf(),
        goal = 50.0,
        launchIntent = null
    )

    override fun supportedTypes(): List<ComplicationType> =
        listOf(
            ComplicationType.SMALL_IMAGE,
            ComplicationType.SHORT_TEXT,
            ComplicationType.LONG_TEXT
        )

    override fun renderShortText(data: Data): ShortTextComplicationData =
        shortText(
            title = data.title,
            text = data.title,
            icon = R.drawable.ic_nordic,
            launchIntent = data.launchIntent
        )

    override fun renderSmallImage(data: Data): SmallImageComplicationData {
        return smallImage(
            icon = Icon.createWithResource(context, R.drawable.ic_nordic),
            type = SmallImageType.ICON,
            name = data.title,
            launchIntent = data.launchIntent
        )
    }

    override fun renderLongText(data: Data): LongTextComplicationData {
        return longText(
            icon = Icon.createWithResource(context, R.drawable.ic_nordic),
            type = SmallImageType.ICON,
            title = data.title,
            text = data.title,
            launchIntent = data.launchIntent
        )
    }
}
