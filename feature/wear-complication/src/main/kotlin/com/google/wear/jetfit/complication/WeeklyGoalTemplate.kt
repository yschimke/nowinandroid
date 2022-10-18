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
import com.google.wear.jetfit.reports.SampleData
import com.google.wear.jetfit.reports.WeeklyProgressReport
import java.time.Instant

public class WeeklyGoalTemplate(
    context: Context,
    val launchIntent: PendingIntent? = null
) :
    TypedComplicationTemplate<WeeklyProgressReport>(context) {

    override fun previewData(): WeeklyProgressReport = SampleData.Weekly

    override fun supportedTypes(): List<ComplicationType> =
        listOf(
            ComplicationType.RANGED_VALUE,
            ComplicationType.SHORT_TEXT,
            ComplicationType.SMALL_IMAGE
        )

    override fun renderRangedValue(data: WeeklyProgressReport): RangedValueComplicationData? {

        return RangedValueComplicationData.Builder(
            data.totalActivityDistance.toFloat(), 0f, data.weeklyGoal.toFloat(), PlainComplicationText.Builder(
                text = "Achieved ${data.percentString}"
            ).build()
        )
            .setText(
                PlainComplicationText.Builder(
                    text = data.percentString
                )
                    .build()
            )
            .setTitle(
                PlainComplicationText.Builder(
                    text = data.title
                )
                    .build()
            )
            .setTapAction(launchIntent)
            .build()
    }

    override fun renderShortText(data: WeeklyProgressReport): ShortTextComplicationData =
        shortText(
            title = data.title,
            text = data.percentString,
            icon = R.drawable.ic_nordic,
            launchIntent = launchIntent
        )

    override fun renderSmallImage(data: WeeklyProgressReport): SmallImageComplicationData {
        return smallImage(
            icon = Icon.createWithResource(context, R.drawable.ic_nordic),
            type = SmallImageType.ICON,
            name = data.percentString,
            launchIntent = launchIntent
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
        data = SampleData.Weekly
    )
}
