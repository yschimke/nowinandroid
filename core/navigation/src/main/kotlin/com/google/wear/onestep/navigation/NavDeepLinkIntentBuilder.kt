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

package com.google.wear.onestep.navigation

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder

public class NavDeepLinkIntentBuilder(
    private val application: Context,
) : IntentBuilder {
    override fun buildActivityListIntent(): PendingIntent {
        // TODO add proper navigation
        val taskDetailIntent = Intent(
            Intent.ACTION_MAIN,
        )

        return TaskStackBuilder.create(application).run {
            addNextIntentWithParentStack(taskDetailIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)!!
        }
    }

    override fun buildActivityIntent(activityId: String): PendingIntent {
        // TODO add proper navigation
        val taskDetailIntent = Intent(
            Intent.ACTION_MAIN,
        )

        return TaskStackBuilder.create(application).run {
            addNextIntentWithParentStack(taskDetailIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)!!
        }
    }
}