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

            for (provider in CronetProvider.getAllProviders(context)) {
                // We're not interested in using the fallback, we're better off sticking with
                // the default OkHttp client in that case.
                if (!provider.isEnabled || provider.name == CronetProvider.PROVIDER_NAME_FALLBACK) {
                    continue
                }
                Log.i("Cronet", "No valid Cronet provider")
                // TODO uncomment once we are sure it's all working
//                return@transform originalClient
            }

            val engine = CronetEngine.Builder(context)
                .enableQuic(true)
                .setUserAgent("Cronet OkHttp Transport Sample")
                .build()

            val cronetInterceptor = CronetInterceptor
                .newBuilder(engine)
                .build()

            Log.i("Cronet", "Using Cronet")

            originalClient.newBuilder()
                .addInterceptor(cronetInterceptor)
                .build()
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
