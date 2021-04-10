package com.bellogate.deeporganic.model

import androidx.annotation.Keep
import java.util.*

@Keep
class User(var name: String,
           var email: String,
           var phoneNumber: String? = "",
           var profilePictureURL: String? = "",
           var address: String? = "",
           var numberOfFollowers: Int? = 0,
           val uniqueId: String = UUID.randomUUID().toString(),
           val timeCreated: Long = System.currentTimeMillis())