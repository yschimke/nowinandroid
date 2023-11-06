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

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okio.Timeout
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

class CronetCallWrapper(
    val request: Request,
    val cronetCallFactoryWrapper: CronetCallFactoryWrapper,
) : Call {
    @Volatile private var canceled = false
    private val executed = AtomicBoolean()
    @Volatile private var realCall: Call? = null

    override fun cancel() {
        if (canceled) return // Already canceled.

        canceled = true
        realCall?.cancel()
    }

    override fun clone(): Call {
        return CronetCallWrapper(request, cronetCallFactoryWrapper)
    }

    override fun enqueue(responseCallback: Callback) {
        check(executed.compareAndSet(false, true)) { "Already Executed" }

        if (canceled) {
            responseCallback.onFailure(this, IOException("Canceled"))
            FailedCall(cronetCallFactoryWrapper, request, "Canceled")
        } else {
            realCall = cronetCallFactoryWrapper.internalCall(request)

            if (canceled) {
                realCall?.cancel()
            }

            realCall!!.enqueue(responseCallback)
        }
    }

    override fun execute(): Response {
        TODO("execute not supported for Cronet bridge")
    }

    override fun isCanceled(): Boolean = canceled

    override fun isExecuted(): Boolean = realCall?.isExecuted() ?: false

    override fun request(): Request = request

    override fun timeout(): Timeout = realCall?.timeout() ?: Timeout.NONE
}