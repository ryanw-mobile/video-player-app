/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.di

import com.rwmobi.dazncodechallenge.data.repository.LocalCacheRepository
import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.data.source.remote.interfaces.RemoteDataSource
import com.rwmobi.dazncodechallenge.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

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
