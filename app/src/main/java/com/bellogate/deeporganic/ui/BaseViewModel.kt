package com.bellogate.deeporganic.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.bellogate.deeporganic.R
import com.bellogate.deeporganic.model.User
import com.bellogate.deeporganic.util.common.SessionManager
import com.bellogate.deeporganic.util.common.signup.SignUpResponseHandler
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

abstract class BaseViewModel: ViewModel() {

    open var signUpResponseHandler: SignUpResponseHandler? = null

    fun  getCurrentUser(): User? = SessionManager.currentUser

    fun signUpOrLoginIntent() = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setTheme(R.style.Theme_DeepOrganic)
        .setAlwaysShowSignInMethodScreen(true)
        .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build(), AuthUI.IdpConfig.EmailBuilder().build()))
        .build()
}