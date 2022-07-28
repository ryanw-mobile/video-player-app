/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import uk.ryanwong.dazn.codechallenge.data.source.local.LocalDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.RemoteDataSource
import uk.ryanwong.dazn.codechallenge.data.source.local.DaznApiDatabase
import uk.ryanwong.dazn.codechallenge.data.source.local.RoomDbDataSource
import uk.ryanwong.dazn.codechallenge.data.source.local.daos.DaznApiDaos
import uk.ryanwong.dazn.codechallenge.data.source.remote.SandBoxAPIDataSource
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