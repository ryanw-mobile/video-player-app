/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import uk.ryanwong.dazn.codechallenge.data.repository.DaznApiRepository
import uk.ryanwong.dazn.codechallenge.data.repository.DefaultCachedRepository
import uk.ryanwong.dazn.codechallenge.data.source.DaznApiDaos
import uk.ryanwong.dazn.codechallenge.data.source.DaznApiDataSource
import uk.ryanwong.dazn.codechallenge.data.source.local.DaznApiDatabase
import uk.ryanwong.dazn.codechallenge.data.source.local.DaznRoomDbDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.DaznSandboxApiDataSource

object ServiceLocator {

    // Database - singleton

    private var daznApiDatabase: DaznApiDatabase? = null
    private fun createDataBase(context: Context): DaznApiDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            DaznApiDatabase::class.java,
            "dazn_api_database"
        )
            .fallbackToDestructiveMigration()
            .build()
        daznApiDatabase = result
        return result
    }

    // Data Sources
    private fun createRoomDBLocalDataSource(context: Context): DaznApiDataSource {
        val database = daznApiDatabase ?: createDataBase(context)
        val daznApiDaos = DaznApiDaos(database.eventsDao, database.scheduleDao)
        return DaznRoomDbDataSource(daznApiDaos)
    }

    private fun createDaznSandboxApiDataSource(): DaznApiDataSource {
        return DaznSandboxApiDataSource()
    }

    // Repository - singleton

    // Annotate the repository with @Volatile because it could get used by multiple threads
    // Only for testing we allow injecting another repository here
    @Volatile
    var daznApiRepository: DaznApiRepository? = null
        @VisibleForTesting set

    fun provideApiRepository(context: Context): DaznApiRepository {
        synchronized(this) {
            return daznApiRepository ?: createApiRepository(context)
        }
    }

    private fun createApiRepository(context: Context): DaznApiRepository {
        val newRepo =
            DefaultCachedRepository(
                createDaznSandboxApiDataSource(),
                createRoomDBLocalDataSource(context)
            )
        daznApiRepository = newRepo
        return newRepo
    }

    // Tests

    private val lock = Any()

    /***
     * Clear all data to avoid test pollution
     */
    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Kotlin usage note
            // apply: Object configuration. Returns Context object
            // run: Object configuration and computing the result. Returns lambda result
            daznApiDatabase?.run {
                clearAllTables()
                close()
            }
            daznApiDatabase = null
            daznApiRepository = null
        }
    }
}