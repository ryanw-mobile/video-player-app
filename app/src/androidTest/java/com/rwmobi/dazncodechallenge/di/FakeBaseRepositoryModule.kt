/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.di

import com.rwmobi.dazncodechallenge.data.repository.FakeRepository
import com.rwmobi.dazncodechallenge.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class],
)
object FakeBaseRepositoryModule {
    @Provides
    @Singleton
    fun provideFakeRepository(): Repository {
        return FakeRepository()
    }
}
