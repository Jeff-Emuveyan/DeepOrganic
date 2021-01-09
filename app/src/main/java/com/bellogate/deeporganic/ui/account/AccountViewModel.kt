package com.bellogate.deeporganic.ui.account

import androidx.lifecycle.ViewModel
import com.bellogate.deeporganic.model.User
import com.bellogate.deeporganic.util.SessionManager

class AccountViewModel : ViewModel() {

    fun  getCurrentUser(): User ? = SessionManager.currentUser

}