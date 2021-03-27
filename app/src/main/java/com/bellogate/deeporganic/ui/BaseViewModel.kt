package com.bellogate.deeporganic.ui

import androidx.lifecycle.ViewModel
import com.bellogate.deeporganic.R
import com.bellogate.deeporganic.model.User
import com.bellogate.deeporganic.util.common.SessionManager
import com.firebase.ui.auth.AuthUI

abstract class BaseViewModel: ViewModel() {

    fun  getCurrentUser(): User? = SessionManager.currentUser

    fun signUpOrLoginIntent() = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setTheme(R.style.Theme_DeepOrganic)
        .setAlwaysShowSignInMethodScreen(true)
        .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build(), AuthUI.IdpConfig.EmailBuilder().build()))
        .build()
}