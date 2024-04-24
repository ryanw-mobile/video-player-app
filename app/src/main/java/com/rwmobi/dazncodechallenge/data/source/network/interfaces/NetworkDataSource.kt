/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.network.interfaces

import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

interface NetworkDataSource {
    suspend fun getEvents(): Result<List<Event>>
    suspend fun getSchedules(): Result<List<Schedule>>
}
