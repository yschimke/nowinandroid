package com.google.wear.ads.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException

/**
 * An [ActivityResultContract] for signing in with the given [GoogleSignInClient].
 */
class GoogleSignInContract(
    private val googleSignInClient: GoogleSignInClient
) : ActivityResultContract<Unit, GoogleSignInAccount?>() {
    override fun createIntent(context: Context, input: Unit): Intent =
        googleSignInClient.signInIntent

    override fun parseResult(resultCode: Int, intent: Intent?): GoogleSignInAccount? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        // As documented, this task must be complete
        check(task.isComplete)

        return if (task.isSuccessful) {
            task.result
        } else {
            val exception = task.exception
            check(exception is ApiException)
            Log.w(
                "GoogleSignInContract",
                "Sign in failed: code=${exception.statusCode}, message=${
                    GoogleSignInStatusCodes.getStatusCodeString(exception.statusCode)
                }"
            )
            null
        }
    }
}