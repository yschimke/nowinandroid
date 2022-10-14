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
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class StandardWeeklyGoalComplicationService() : SuspendingComplicationDataSourceService() {
    @Inject
    lateinit var intentBuilder: IntentBuilder

    @Inject
    lateinit var activityRepository: ActivityRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    fun previewData(): Data = Data(
        title = "OneStep",
        activities = listOf(),
        goal = 50.0,
        launchIntent = null
    )

    suspend fun data(): Data {
        val today = LocalDate.now()
        val activities =
            activityRepository.getCompletedActivitiesInPeriod(today.minusWeeks(1), today)
        val weeklyGoal = settingsRepository.getWeeklyGoal()

        return Data(
            "Weekly Activities",
            activities,
            weeklyGoal,
            intentBuilder.buildActivityListIntent()
        )
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        return renderComplicationData(type, previewData())
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        return renderComplicationData(request.complicationType, data())
    }

    private fun renderComplicationData(type: ComplicationType, data: Data): ComplicationData? {
        return when (type) {
            ComplicationType.RANGED_VALUE -> renderRangedValue(data)
            ComplicationType.SHORT_TEXT -> renderShortText(data)
            ComplicationType.SMALL_IMAGE -> renderSmallImage(data)
            else -> NoDataComplicationData()
        }
    }

    fun renderRangedValue(data: Data): RangedValueComplicationData {
        return RangedValueComplicationData.Builder(
            data.value.toFloat(), 0f, data.goal.toFloat(), PlainComplicationText.Builder(
                text = "Achieved ${data.percent}"
            ).build()
        )
            .setText(
                PlainComplicationText.Builder(
                    text = data.percent
                ).build()
            )
            .setTitle(
                PlainComplicationText.Builder(
                    text = data.title
                ).build()
            )
            .setTapAction(data.launchIntent)
            .build()
    }

    fun renderShortText(data: Data): ShortTextComplicationData =
        ShortTextComplicationData.Builder(
            PlainComplicationText.Builder(text = data.percent)
                .build(),
            PlainComplicationText.Builder(
                text = data.percent
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
                data.launchIntent
            )
            .build()

    fun renderSmallImage(data: Data): SmallImageComplicationData {
        return SmallImageComplicationData.Builder(
            smallImage = SmallImage.Builder(
                image = Icon.createWithResource(this, R.drawable.ic_nordic),
                type = SmallImageType.ICON
            )
                .build(),
            contentDescription = PlainComplicationText.Builder(
                text = data.percent
            )
                .build()
        )
            .setTapAction(
                tapAction = data.launchIntent
            )
            .build()
    }

}
