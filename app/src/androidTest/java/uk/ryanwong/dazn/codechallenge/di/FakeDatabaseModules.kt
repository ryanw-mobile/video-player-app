/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import uk.ryanwong.dazn.codechallenge.data.source.local.DaznApiDatabase
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModules::class],
)
object FakeDatabaseModules {
    // Using an in-memory database for testing, because it doesn't survive killing the process.
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
    ): DaznApiDatabase {
        return Room.inMemoryDatabaseBuilder(
            appContext,
            DaznApiDatabase::class.java,
        ).allowMainThreadQueries().build()
    }
}
