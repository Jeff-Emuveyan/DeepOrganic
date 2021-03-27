package com.bellogate.deeporganic.util.common

import androidx.annotation.Keep
import com.bellogate.deeporganic.model.User

@Keep
object SessionManager {

    var  currentUser: User? = null
}