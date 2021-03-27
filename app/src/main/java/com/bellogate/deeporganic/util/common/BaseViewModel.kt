package com.bellogate.deeporganic.util.common

import androidx.lifecycle.ViewModel
import com.bellogate.deeporganic.model.User
import com.firebase.ui.auth.AuthUI

abstract class BaseViewModel: ViewModel() {

    fun  getCurrentUser(): User? = SessionManager.currentUser

    fun signUpOrLoginIntent() = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build(), AuthUI.IdpConfig.EmailBuilder().build()))
        .build()
}