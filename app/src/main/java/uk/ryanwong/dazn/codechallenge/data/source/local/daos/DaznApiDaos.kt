/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.local.daos

/***
 * This data class is designed to make a balance between splitting Daos for easier maintenance
 * and avoiding a need to pass a long list of DAOs through the constructor of DaznRoomDbDataSource.
 */
data class DaznApiDaos(
    val eventsDao: EventsDao,
    val scheduleDao: ScheduleDao
)
