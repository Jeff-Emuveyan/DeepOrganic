package com.bellogate.deeporganic.model

import androidx.annotation.Keep

@Keep
class User(var name: String,
           var email: String,
           var phoneNumber: String? = "",
           var profilePictureURL: String? = "",
           var address: String? = "",
           var numberOfFollowers: Int? = 0)