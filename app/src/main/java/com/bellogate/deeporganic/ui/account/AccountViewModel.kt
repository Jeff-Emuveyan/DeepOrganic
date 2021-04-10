package com.bellogate.deeporganic.ui.account

import androidx.hilt.lifecycle.ViewModelInject
import com.bellogate.deeporganic.ui.BaseViewModel
import com.bellogate.deeporganic.util.common.signup.SignUpResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(override var signUpResponseHandler: SignUpResponseHandler?) : BaseViewModel() {
}