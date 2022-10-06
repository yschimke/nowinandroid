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

/**
 * Strategy for generating a PendingIntent links directly to the Player or Downloads View.
 */
public interface IntentBuilder {
    /**
     * Pending intent for the screen showing downloads.
     * Used in background Notifications for download progress.
     */
    public fun buildActivityListIntent(): PendingIntent

    /**
     * Pending intent for the screen showing playback controls.
     * Used in Media Notification and System Controls.
     */
    public fun buildActivityIntent(activityId: String): PendingIntent
}
