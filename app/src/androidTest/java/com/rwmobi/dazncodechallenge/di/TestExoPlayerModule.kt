/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.di

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.test.utils.TestExoPlayerBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [ExoPlayerModule::class],
)
object TestExoPlayerModule {

    @Provides
    @ViewModelScoped
    fun provideExoPlayer(
        @ApplicationContext appContext: Context,
    ): Player = TestExoPlayerBuilder(appContext).build()
}
