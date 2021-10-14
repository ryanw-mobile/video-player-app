/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ryanwong.dazn.codechallenge.base.BaseLocalDataSource
import uk.ryanwong.dazn.codechallenge.base.BaseRemoteDataSource
import uk.ryanwong.dazn.codechallenge.data.source.local.RoomDbDataSource
import uk.ryanwong.dazn.codechallenge.data.source.local.daos.DaznApiDaos
import uk.ryanwong.dazn.codechallenge.data.source.remote.SandBoxAPIDataSource
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModules {

    @Provides
    @Singleton
    fun provideLocalDataSource(
        daznApiDaos: DaznApiDaos
    ): BaseLocalDataSource {
        return RoomDbDataSource(daznApiDaos)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(): BaseRemoteDataSource {
        return SandBoxAPIDataSource()
    }


}