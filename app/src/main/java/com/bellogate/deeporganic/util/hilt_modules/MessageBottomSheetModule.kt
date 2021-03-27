package com.bellogate.deeporganic.util.hilt_modules

import androidx.annotation.Keep
import com.bellogate.deeporganic.ui.message.MessageBottomSheet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Keep
@Module
@InstallIn(ActivityComponent::class)
object MessageBottomSheetModule {

    @Provides
    fun provideMessageBottomSheet(
        // Potential dependencies of this type
    ): MessageBottomSheet {
        return MessageBottomSheet(null)
    }
}