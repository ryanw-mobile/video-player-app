/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import uk.ryanwong.dazn.codechallenge.data.repository.BaseRepository
import uk.ryanwong.dazn.codechallenge.data.repository.FakeRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [BaseRepositoryModules::class]
)
object FakeBaseRepositoryModules {

    @Provides
    @Singleton
    fun provideFakeRepository(): BaseRepository {
        synchronized(this) {
            return FakeRepository()
        }
    }
}