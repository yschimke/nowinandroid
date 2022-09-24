package com.google.wear.ads.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

data class LoginScreenState(
    val account: GoogleSignInAccount? = null
)
