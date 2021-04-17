package com.bellogate.deeporganic.ui.account

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import com.bellogate.deeporganic.data.user.UserRepository
import com.bellogate.deeporganic.ui.BaseViewModel
import com.bellogate.deeporganic.util.common.signup.SignUpResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(override var signUpResponseHandler: SignUpResponseHandler?,
                                           override var  userRepository: UserRepository?) : BaseViewModel() {


    suspend fun signOut(context: Context) {
        userRepository?.signOut(context)
    }
}