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
import uk.ryanwong.dazn.codechallenge.base.BaseRepository
import uk.ryanwong.dazn.codechallenge.data.repository.DefaultCachedRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object BaseRepositoryModules {

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: BaseRemoteDataSource,
        localDataSource: BaseLocalDataSource
    ): BaseRepository {
        synchronized(this) {
            return DefaultCachedRepository(remoteDataSource, localDataSource)
        }
    }
}