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

package com.google.wear.jetfit.complication

import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.NoDataComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import coil.ImageLoader
import com.google.wear.jetfit.core.compose.R
import com.google.wear.jetfit.data.repository.ActivityRepository
import com.google.wear.jetfit.data.repository.SettingsRepository
import com.google.wear.jetfit.navigation.IntentBuilder
import com.google.wear.jetfit.reports.SampleData
import com.google.wear.jetfit.reports.WeeklyProgressReport
import com.google.wear.jetfit.reports.WeeklyProgressUseCase
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class StandardWeeklyGoalComplicationService() : SuspendingComplicationDataSourceService() {
    @Inject
    lateinit var intentBuilder: IntentBuilder

    @Inject
    lateinit var weeklyProgressUseCase: WeeklyProgressUseCase

    fun previewData(): WeeklyProgressReport = SampleData.Weekly

    suspend fun data(): WeeklyProgressReport {
        return weeklyProgressUseCase()
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        return renderComplicationData(type, previewData())
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        return renderComplicationData(request.complicationType, data())
    }

    private fun renderComplicationData(
        type: ComplicationType,
        data: WeeklyProgressReport
    ): ComplicationData? {
        return when (type) {
            ComplicationType.RANGED_VALUE -> renderRangedValue(data)
            ComplicationType.SHORT_TEXT -> renderShortText(data)
            ComplicationType.SMALL_IMAGE -> renderSmallImage(data)
            else -> NoDataComplicationData()
        }
    }

    fun renderRangedValue(data: WeeklyProgressReport): RangedValueComplicationData {
        return RangedValueComplicationData.Builder(
            data.totalActivityDistance.toFloat(),
            0f,
            data.weeklyGoal.toFloat(),
            PlainComplicationText.Builder(
                text = "Achieved ${data.percentString}"
            ).build()
        )
            .setText(
                PlainComplicationText.Builder(
                    text = data.percentString
                ).build()
            )
            .setTitle(
                PlainComplicationText.Builder(
                    text = data.title
                ).build()
            )
            .setTapAction(intentBuilder.buildActivityListIntent())
            .build()
    }

    fun renderShortText(data: WeeklyProgressReport): ShortTextComplicationData =
        ShortTextComplicationData.Builder(
            PlainComplicationText.Builder(text = data.percentString)
                .build(),
            PlainComplicationText.Builder(
                text = data.percentString
            ).build()
        )
            .setMonochromaticImage(
                MonochromaticImage.Builder(
                    Icon.createWithResource(
                        this@StandardWeeklyGoalComplicationService,
                        R.drawable.ic_nordic
                    )
                )
                    .build()
            )
            .setTitle(
                PlainComplicationText.Builder(
                    text = data.title
                )
                    .build()
            )
            .setTapAction(
                intentBuilder.buildActivityListIntent()
            )
            .build()

    fun renderSmallImage(data: WeeklyProgressReport): SmallImageComplicationData {
        return SmallImageComplicationData.Builder(
            smallImage = SmallImage.Builder(
                image = Icon.createWithResource(this, R.drawable.ic_nordic),
                type = SmallImageType.ICON
            )
                .build(),
            contentDescription = PlainComplicationText.Builder(
                text = data.percentString
            )
                .build()
        )
            .setTapAction(
                tapAction = intentBuilder.buildActivityListIntent()
            )
            .build()
    }

}
