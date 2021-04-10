package com.bellogate.deeporganic.util.hilt_modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object FirebaseUserModule {

    @Provides
    fun providesFirebaseUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

}