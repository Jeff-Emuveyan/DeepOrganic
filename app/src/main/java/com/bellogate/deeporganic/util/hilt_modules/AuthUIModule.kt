package com.bellogate.deeporganic.util.hilt_modules

import com.firebase.ui.auth.AuthUI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AuthUIModule {

    @Provides
    fun providesAuthUI(): AuthUI {
        return AuthUI.getInstance()
    }
}
