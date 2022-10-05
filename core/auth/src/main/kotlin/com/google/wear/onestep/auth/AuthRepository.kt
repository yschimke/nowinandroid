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

package com.google.wear.onestep.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    @ServerClientId serverClientId: String?,
    @ApplicationContext applicationContext: Context,
): TokenAccess {
    val googleApiClient =
        GoogleSignIn.getClient(
            applicationContext,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .apply {
                    if (serverClientId != null) {
                        requestIdToken(serverClientId)
                    }
                }

                .build()
        )

    val state = MutableStateFlow<GoogleSignInAccount?>(null)

    init {
        state.update {
            GoogleSignIn.getLastSignedInAccount(applicationContext)
        }
    }

    suspend fun updateAccount(account: GoogleSignInAccount?) {
        state.value = account
    }

    suspend fun logout() {
        googleApiClient.signOut().await()
        updateAccount(null)
    }

    override val idToken: String? = state.value?.idToken
}
