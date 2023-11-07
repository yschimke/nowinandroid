/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.core.network.di

import android.content.Context
import android.util.Log
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import com.google.samples.apps.nowinandroid.core.network.BuildConfig
import com.google.samples.apps.nowinandroid.core.network.cronet.CronetCallFactoryWrapper
import com.google.samples.apps.nowinandroid.core.network.fake.FakeAssetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesFakeAssetManager(
        @ApplicationContext context: Context,
    ): FakeAssetManager = FakeAssetManager(context.assets::open)

    @Provides
    @Singleton
    fun okHttpCallFactory(@ApplicationContext context: Context): Call.Factory =
        CronetCallFactoryWrapper(
            context,
            OkHttpClient.Builder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(
                            HttpLoggingInterceptor()
                                .apply {
                                    setLevel(BODY)
                                },
                        )
                        addInterceptor { chain ->
                            Log.i(
                                "Cronet",
                                "Request ${chain.request().url} ${chain.request().headers}",
                            )

                            chain.proceed(chain.request()).also { response ->
                                val protocol = response.protocol
                                Log.i(
                                    "Cronet",
                                    "Protocol $protocol for ${response.request.url.host}",
                                )
                            }
                        }
                    }
                }
                .build(),
        )

    /**
     * Since we're displaying SVGs in the app, Coil needs an ImageLoader which supports this
     * format. During Coil's initialization it will call `applicationContext.newImageLoader()` to
     * obtain an ImageLoader.
     *
     * @see <a href="https://github.com/coil-kt/coil/blob/main/coil-singleton/src/main/java/coil/Coil.kt">Coil</a>
     */
    @Provides
    @Singleton
    fun imageLoader(
        okHttpCallFactory: Call.Factory,
        @ApplicationContext application: Context,
    ): ImageLoader = ImageLoader.Builder(application)
        .callFactory(okHttpCallFactory)
        .components {
            add(SvgDecoder.Factory())
        }
        // Assume most content images are versioned urls
        // but some problematic images are fetching each time
        .respectCacheHeaders(false)
        .apply {
            if (BuildConfig.DEBUG) {
                logger(DebugLogger())
            }
        }
        .build()
}
