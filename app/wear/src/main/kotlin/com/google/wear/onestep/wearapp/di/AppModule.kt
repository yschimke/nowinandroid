/*
 * Copyright 2021 Google LLC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.wear.onestep.wearapp.di

import android.content.Context
import com.google.wear.onestep.BuildConfig
import com.google.wear.onestep.auth.ServerClientId
import com.google.wear.onestep.navigation.IntentBuilder
import com.google.wear.onestep.navigation.NavDeepLinkIntentBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Singleton
    @Provides
    fun moshi(
    ): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun intentBuilder(
        @ApplicationContext application: Context,
    ): IntentBuilder {
        return NavDeepLinkIntentBuilder(application)
    }

    @Suppress("KotlinConstantConditions")
    @Singleton
    @Provides
    @ServerClientId
    fun serverClientId(): String? = BuildConfig.serverClientId.let {
        if (it == "none" || it.isEmpty())
            null
        else
            it
    }
}
