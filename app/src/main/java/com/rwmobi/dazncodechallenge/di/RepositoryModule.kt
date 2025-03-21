/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.di

import com.rwmobi.dazncodechallenge.data.repository.LocalCacheRepository
import com.rwmobi.dazncodechallenge.data.source.local.interfaces.LocalDataSource
import com.rwmobi.dazncodechallenge.data.source.network.interfaces.NetworkDataSource
import com.rwmobi.dazncodechallenge.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        networkDataSource: NetworkDataSource,
        localDataSource: LocalDataSource,
        @DispatcherModule.IoDispatcher dispatcher: CoroutineDispatcher,
    ): Repository {
        return LocalCacheRepository(
            networkDataSource = networkDataSource,
            localDataSource = localDataSource,
            dispatcher = dispatcher,
        )
    }
}
