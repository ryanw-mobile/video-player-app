/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.di

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import coil3.ImageLoader
import coil3.test.FakeImageLoaderEngine
import coil3.test.default
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CoilModule::class],
)
object FakeCoilModule {
    @Singleton
    @Provides
    fun provideFakeCoilImageLoader(@ApplicationContext context: Context): ImageLoader {
        val sampleDrawable = ColorDrawable(Color.BLACK)
        val engine = FakeImageLoaderEngine.Builder()
            .default(drawable = sampleDrawable)
            .build()
        return ImageLoader.Builder(context)
            .components { add(engine) }
            .build()
    }
}
