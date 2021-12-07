/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ryanwong.dazn.codechallenge.data.source.local.LocalDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.RemoteDataSource
import uk.ryanwong.dazn.codechallenge.data.repository.Repository
import uk.ryanwong.dazn.codechallenge.data.repository.LocalCacheRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModules {

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): Repository {
        synchronized(this) {
            return LocalCacheRepository(remoteDataSource, localDataSource)
        }
    }
}