/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import uk.ryanwong.dazn.codechallenge.data.repository.LocalCacheRepository
import uk.ryanwong.dazn.codechallenge.data.repository.Repository
import uk.ryanwong.dazn.codechallenge.data.source.local.LocalDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.RemoteDataSource

@InstallIn(ViewModelComponent::class)
@Module
object RepositoryModules {
    @Provides
    @ViewModelScoped
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
    ): Repository {
        synchronized(this) {
            return LocalCacheRepository(remoteDataSource, localDataSource)
        }
    }
}
