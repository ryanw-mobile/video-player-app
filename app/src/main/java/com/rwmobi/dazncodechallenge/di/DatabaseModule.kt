/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.di

import android.content.Context
import androidx.room.Room
import com.rwmobi.dazncodechallenge.data.source.local.DaznApiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
    ): DaznApiDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            DaznApiDatabase::class.java,
            "dazn_api_database",
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
