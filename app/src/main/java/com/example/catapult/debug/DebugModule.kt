package com.example.catapult.debug

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DebugModule {

    @Singleton
    @Provides
    fun provideErrorTracker(): ErrorTracker {
        return ErrorTracker()
    }

}