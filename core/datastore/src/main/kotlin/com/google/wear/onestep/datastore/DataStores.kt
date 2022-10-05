package com.google.wear.onestep.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.google.wear.onestep.data.ActivityStatusSerializer
import com.google.wear.onestep.data.SettingsSerializer
import com.google.wear.onestep.proto.Api.ActivityStatus
import com.google.wear.onestep.proto.Api.Settings

val Context.activityStatusStore: DataStore<ActivityStatus> by dataStore(
    fileName = "activityStatus.pb",
    serializer = ActivityStatusSerializer
)

val Context.settingsStore: DataStore<Settings> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsSerializer
)