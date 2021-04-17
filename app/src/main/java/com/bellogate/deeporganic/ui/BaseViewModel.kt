package com.bellogate.deeporganic.ui

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bellogate.deeporganic.R
import com.bellogate.deeporganic.data.user.UserRepository
import com.bellogate.deeporganic.model.User
import com.bellogate.deeporganic.util.common.SessionManager
import com.bellogate.deeporganic.util.common.signup.SignUpResponseHandler
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

abstract class BaseViewModel: ViewModel() {

    open var signUpResponseHandler: SignUpResponseHandler? = null
    open var userRepository: UserRepository? = null
    private val _userSignedOut = MutableLiveData<Boolean>().apply { value = false }
    val userSignedOut: LiveData<Boolean> = _userSignedOut

    fun signUpOrLoginIntent() = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setTheme(R.style.Theme_DeepOrganic)
        .setAlwaysShowSignInMethodScreen(true)
        .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build(), AuthUI.IdpConfig.EmailBuilder().build()))
        .build()

    /**
     * Used to know whether the user has signed out
     **/
    fun listenForUserSignOut(sessionManager: SessionManager?){
        if(sessionManager == null || userRepository == null) return

        userRepository?.listenForUserSignOut {
            if(!it){//user has Signed Out.
                //delete user from db:
                userRepository?.deleteUser(sessionManager.currentUser?.email)
                _userSignedOut.value = true
            }else{
                _userSignedOut.value = false
            }
        }
    }
}