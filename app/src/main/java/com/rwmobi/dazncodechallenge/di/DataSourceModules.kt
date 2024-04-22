/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.di

import com.rwmobi.dazncodechallenge.data.source.local.DaznApiDatabase
import com.rwmobi.dazncodechallenge.data.source.local.RoomDbDataSource
import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.data.source.network.DaznApiService
import com.rwmobi.dazncodechallenge.data.source.network.SandBoxAPIDataSource
import com.rwmobi.dazncodechallenge.data.source.network.interfaces.NetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModules {
    @Provides
    @Singleton
    fun provideLocalDataSource(
        database: DaznApiDatabase,
        @DispatcherModule.IoDispatcher dispatcher: CoroutineDispatcher,
    ): LocalDataSource {
        return RoomDbDataSource(
            eventsDao = database.eventsDao,
            scheduleDao = database.scheduleDao,
            dispatcher = dispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideNetworkDataSource(
        retrofitService: DaznApiService,
        @DispatcherModule.IoDispatcher dispatcher: CoroutineDispatcher,
    ): NetworkDataSource {
        return SandBoxAPIDataSource(
            retrofitService = retrofitService,
            dispatcher = dispatcher,
        )
    }
}
