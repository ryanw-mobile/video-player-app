/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package com.rwmobi.dazncodechallenge.di

import android.content.Context
import androidx.room.Room
import com.rwmobi.dazncodechallenge.data.source.local.DaznApiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class],
)
object FakeDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
    ): DaznApiDatabase {
        return Room.inMemoryDatabaseBuilder(
            appContext,
            DaznApiDatabase::class.java,
        ).allowMainThreadQueries().build()
    }
}
