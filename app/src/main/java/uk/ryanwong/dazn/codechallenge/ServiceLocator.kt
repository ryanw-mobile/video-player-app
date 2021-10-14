/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import uk.ryanwong.dazn.codechallenge.base.BaseLocalDataSource
import uk.ryanwong.dazn.codechallenge.base.BaseRemoteDataSource
import uk.ryanwong.dazn.codechallenge.base.BaseRepository
import uk.ryanwong.dazn.codechallenge.data.repository.DefaultCachedRepository
import uk.ryanwong.dazn.codechallenge.data.source.local.daos.DaznApiDaos
import uk.ryanwong.dazn.codechallenge.data.source.local.DaznApiDatabase
import uk.ryanwong.dazn.codechallenge.data.source.local.RoomDbDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.SandBoxAPIDataSource

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
    private fun createLocalDataSource(context: Context): BaseLocalDataSource {
        val database = daznApiDatabase ?: createDataBase(context)
        val daznApiDaos = DaznApiDaos(database.eventsDao, database.scheduleDao)
        return RoomDbDataSource(daznApiDaos)
    }

    private fun createRemoteDataSource(): BaseRemoteDataSource {
        return SandBoxAPIDataSource()
    }

    // Repository - singleton

    // Annotate the repository with @Volatile because it could get used by multiple threads
    // Only for testing we allow injecting another repository here
    @Volatile
    var baseRepository: BaseRepository? = null
        @VisibleForTesting set

    fun provideApiRepository(context: Context): BaseRepository {
        synchronized(this) {
            return baseRepository ?: createApiRepository(context)
        }
    }

    private fun createApiRepository(context: Context): BaseRepository {
        val newRepo =
            DefaultCachedRepository(
                createRemoteDataSource(),
                createLocalDataSource(context)
            )
        baseRepository = newRepo
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
            baseRepository = null
        }
    }
}