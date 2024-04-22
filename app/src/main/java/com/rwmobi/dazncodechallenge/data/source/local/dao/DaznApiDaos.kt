/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.local.dao

/***
 * This data class is designed to make a balance between splitting Daos for easier maintenance
 * and avoiding a need to pass a long list of DAOs through the constructor of DaznRoomDbDataSource.
 */
data class DaznApiDaos(
    val eventsDao: EventsDao,
    val scheduleDao: ScheduleDao,
)
