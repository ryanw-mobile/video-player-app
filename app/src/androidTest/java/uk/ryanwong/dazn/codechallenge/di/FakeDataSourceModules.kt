/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.Dispatchers
import uk.ryanwong.dazn.codechallenge.base.BaseLocalDataSource
import uk.ryanwong.dazn.codechallenge.base.BaseRemoteDataSource
import uk.ryanwong.dazn.codechallenge.data.repository.FakeLocalDataSource
import uk.ryanwong.dazn.codechallenge.data.repository.FakeRemoteDataSource
import uk.ryanwong.dazn.codechallenge.data.source.local.DaznApiDatabase
import uk.ryanwong.dazn.codechallenge.data.source.local.RoomDbDataSource
import uk.ryanwong.dazn.codechallenge.data.source.local.daos.DaznApiDaos
import uk.ryanwong.dazn.codechallenge.data.source.remote.SandBoxAPIDataSource
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class ProvideFakedLocalDataSource

@Qualifier
annotation class ProvideRoomDbDataSource

@Qualifier
annotation class ProvideFakedRemoteDataSource

@Qualifier
annotation class ProvideSandBoxAPIDataSource

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataSourceModules::class]
)
object FakeDataSourceModules {

    @Provides
    @Singleton
    fun provideDaznApiDaos(database: DaznApiDatabase): DaznApiDaos {
        return DaznApiDaos(database.eventsDao, database.scheduleDao)
    }

    @Provides
    @ProvideFakedLocalDataSource
    @Singleton
    fun provideFakedLocalDataSource(): BaseLocalDataSource {
        return FakeLocalDataSource()
    }

    @Provides
    @ProvideRoomDbDataSource
    @Singleton
    fun provideLocalDataSource(
        daznApiDaos: DaznApiDaos
    ): BaseLocalDataSource {
        return RoomDbDataSource(daznApiDaos, Dispatchers.Main)
    }

    @Provides
    @ProvideFakedRemoteDataSource
    @Singleton
    fun provideFakedRemoteDataSource(): BaseRemoteDataSource {
        return FakeRemoteDataSource()
    }

    @Provides
    @ProvideSandBoxAPIDataSource
    @Singleton
    fun provideRemoteDataSource(): BaseRemoteDataSource {
        return SandBoxAPIDataSource()
    }
}