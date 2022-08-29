/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import uk.ryanwong.dazn.codechallenge.data.repository.FakeRepository
import uk.ryanwong.dazn.codechallenge.data.repository.Repository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModules::class]
)
object FakeBaseRepositoryModules {

    @Provides
    @Singleton
    fun provideFakeRepository(): Repository {
        synchronized(this) {
            return FakeRepository()
        }
    }
}
