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

package com.google.wear.jetfit.complication;

import android.app.PendingIntent
import android.content.Context
import android.graphics.drawable.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import com.google.android.horologist.compose.tools.ComplicationRendererPreview
import com.google.android.horologist.tiles.complication.TypedComplicationTemplate
import com.google.wear.jetfit.core.compose.R
import com.google.wear.jetfit.data.room.CompletedActivity
import java.time.Instant

public class WeeklyGoalTemplate(
    context: Context
) :
    TypedComplicationTemplate<WeeklyGoalTemplate.Data>(context) {
    data class Data(
        val title: String,
        val activities: List<CompletedActivity>,
        val goal: Double,
        val launchIntent: PendingIntent?
    ) {
        val value = activities.sumOf { it.distance }
        val percent: String = "${(value / goal * 100f).toInt()}%"
    }

    override fun previewData(): Data = Data(
        title = "OneStep",
        activities = listOf(),
        goal = 50.0,
        launchIntent = null
    )

    override fun supportedTypes(): List<ComplicationType> =
        listOf(
            ComplicationType.RANGED_VALUE,
            ComplicationType.SHORT_TEXT,
            ComplicationType.SMALL_IMAGE
        )

    override fun renderRangedValue(data: Data): RangedValueComplicationData? {

        return RangedValueComplicationData.Builder(
            data.value.toFloat(), 0f, data.goal.toFloat(), PlainComplicationText.Builder(
                text = "Achieved ${data.percent}"
            ).build()
        )
            .setText(
                PlainComplicationText.Builder(
                    text = data.percent
                )
                    .build()
            )
            .setTitle(
                PlainComplicationText.Builder(
                    text = data.title
                )
                    .build()
            )
            .setTapAction(data.launchIntent)
            .build()
    }

    override fun renderShortText(data: Data): ShortTextComplicationData =
        shortText(
            title = data.title,
            text = data.percent,
            icon = R.drawable.ic_nordic,
            launchIntent = data.launchIntent
        )

    override fun renderSmallImage(data: Data): SmallImageComplicationData {
        return smallImage(
            icon = Icon.createWithResource(context, R.drawable.ic_nordic),
            type = SmallImageType.ICON,
            name = data.percent,
            launchIntent = data.launchIntent
        )
    }
}

@Composable
@Preview(
    backgroundColor = 0xFF000000,
    showBackground = true,
)
fun AppLaunchComplicationPreviewDefaultDataTemp() {
    val context = LocalContext.current
    val renderer = WeeklyGoalTemplate(context)

    ComplicationRendererPreview(
        complicationRenderer = renderer,
        data = WeeklyGoalTemplate.Data(
            title = "OneStep",
            activities = listOf(CompletedActivity("1", "Running", 30.0, Instant.now())),
            goal = 50.0,
            launchIntent = null
        )
    )
}
