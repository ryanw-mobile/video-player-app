/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.di

import android.content.Context
import com.rwmobi.dazncodechallenge.data.source.local.DaznApiDatabase
import com.rwmobi.dazncodechallenge.data.source.local.RoomDbDataSource
import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.data.source.network.AssetFileDataSource
import com.rwmobi.dazncodechallenge.data.source.network.interfaces.NetworkDataSource
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {
    @Provides
    @Singleton
    fun provideLocalDataSource(
        database: DaznApiDatabase,
        @DispatcherModule.IoDispatcher dispatcher: CoroutineDispatcher,
    ): LocalDataSource = RoomDbDataSource(
        eventsDao = database.eventsDao,
        scheduleDao = database.scheduleDao,
        dispatcher = dispatcher,
    )

    @Provides
    @Singleton
    fun provideNetworkDataSource(
        @ApplicationContext context: Context,
        moshi: Moshi,
        @DispatcherModule.IoDispatcher dispatcher: CoroutineDispatcher,
    ): NetworkDataSource = AssetFileDataSource(
        context = context,
        moshi = moshi,
        dispatcher = dispatcher,
    )
}
