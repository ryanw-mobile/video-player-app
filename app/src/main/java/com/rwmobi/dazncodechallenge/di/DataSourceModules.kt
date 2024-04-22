/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.di

import com.rwmobi.dazncodechallenge.data.source.local.DaznApiDatabase
import com.rwmobi.dazncodechallenge.data.source.local.RoomDbDataSource
import com.rwmobi.dazncodechallenge.data.source.local.dao.DaznApiDaos
import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.data.source.remote.SandBoxAPIDataSource
import com.rwmobi.dazncodechallenge.data.source.remote.interfaces.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModules {
    @Provides
    @Singleton
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun provideDaznApiDaos(database: DaznApiDatabase): DaznApiDaos {
        return DaznApiDaos(database.eventsDao, database.scheduleDao)
    }
}

@InstallIn(SingletonComponent::class)
@Module
abstract class LocalDataSourceModule {
    @Binds
    abstract fun bindLocalDataSource(impl: RoomDbDataSource): LocalDataSource
}

@InstallIn(SingletonComponent::class)
@Module
abstract class RemoteDataSourceModule {
    @Binds
    abstract fun bindRemoteDataSource(impl: SandBoxAPIDataSource): RemoteDataSource
}
