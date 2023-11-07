/*
 * Copyright 2023 The Android Open Source Project
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

package com.google.samples.apps.nowinandroid.core.network.cronet

import android.content.Context
import android.util.Log
import com.google.android.gms.net.CronetProviderInstaller
import com.google.common.util.concurrent.Futures.transform
import com.google.common.util.concurrent.ListenableFuture
import com.google.net.cronet.okhttptransport.CronetInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import org.chromium.net.CronetEngine
import org.chromium.net.CronetProvider
import java.net.URL
import java.util.concurrent.TimeUnit.MILLISECONDS

class CronetCallFactoryWrapper(context: Context, val originalClient: OkHttpClient) : Call.Factory {
    init {
        Log.i("Cronet", "Init CronetCallFactoryWrapper")
    }

    val cronetInitFuture = CronetProviderInstaller.installProvider(context).toFuture()
    val cronetEngineFuture: ListenableFuture<Call.Factory> = transform(
        cronetInitFuture,
        {
            Log.i("Cronet", "CronetProviderInstaller.installProvider complete")

            val allProviders = CronetProvider.getAllProviders(context)
            val info = allProviders.map { it.name + if (it.isEnabled) "/enabled" else "/disabled" }
            Log.i("Cronet", "Checking $info")

            val validProvider = allProviders.firstOrNull { it.isEnabled && it.name != CronetProvider.PROVIDER_NAME_FALLBACK }

            Log.i("Cronet", "Provider: ${validProvider?.name}")

            if (validProvider == null) {
                Log.i("Cronet", "Using OkHttp")

                originalClient
            } else {
                Log.i("Cronet", "Using Cronet")

                val engine = validProvider.createBuilder()
                    .apply {
                        enableQuic(true)
                        enableBrotli(true)
                        enableHttp2(true)

                        setUserAgent("Cronet OkHttp Transport Sample")

                        setStoragePath(
                            context.cacheDir.resolve("okhttpCronetProvider").apply {
                                mkdirs()
                            }.absolutePath
                        )
                        enableHttpCache(CronetEngine.Builder.HTTP_CACHE_DISK, 10_000_000)

                        addQuicHint("firebasestorage.googleapis.com", 443, 443)
                        addQuicHint(".googleapis.com", 443, 443)
                        addQuicHint(".google.com", 443, 443)
                        addQuicHint("www.google.com", 443, 443)
                        addQuicHint("storage.googleapis.com", 443, 443)
                        addQuicHint("www.googleapis.com", 443, 443)
                        addQuicHint(".googleusercontent.com", 443, 443)
                        addQuicHint(".ytimg.com", 443, 443)
                    }
                    .build()

                // enable for Firebase
                URL.setURLStreamHandlerFactory(engine.createURLStreamHandlerFactory())

                val cronetInterceptor = CronetInterceptor
                    .newBuilder(engine)
                    .build()

                Log.i("Cronet", "Using Cronet")

                originalClient.newBuilder()
                    .addInterceptor(cronetInterceptor)
                    .build()
            }
        },
        Dispatchers.Default.asExecutor(),
    )
    
    val clientWaitMs = 250L

    override fun newCall(request: Request): Call {
        try {
            return CronetCallWrapper(request, this)
        } catch (e: Exception) {
            return FailedCall(this, request, "Failed to init cronet")
        }
    }

    internal fun internalCall(request: Request): Call {
        if (!cronetEngineFuture.isDone) {
            Log.i("Cronet", "waiting for cronet")
        }
        val client = try {
            cronetEngineFuture.get(clientWaitMs, MILLISECONDS)
        } catch (e: Exception) {
            Log.i("Cronet", "exception", e)
            // TODO log
            originalClient
        }

        return client.newCall(request)
    }
}
