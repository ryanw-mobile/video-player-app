/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.domain.repository

import androidx.lifecycle.LiveData
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.model.Schedule

interface Repository {
    // The LiveData implementations are designed to be used by ViewModels
    fun observeEvents(): LiveData<List<Event>>

    fun observeSchedule(): LiveData<List<Schedule>>

    // The static versions are designed to be used for tests
    suspend fun getEvents(): List<Event>

    suspend fun getSchedule(): List<Schedule>

    // The functions exposed to ViewModels
    // They don't have to care about where we pulled the data from
    suspend fun refreshEvents()

    suspend fun refreshSchedule()
}
