/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uk.ryanwong.dazn.codechallenge.base.BaseRepository
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import java.io.IOException

class FakeRepository : BaseRepository {

    // Use simple data structures to provide a controlled set of data for testing ViewModel
    private val observableEvents = MutableLiveData<List<Event>>().apply {
        value = listOf()
    }
    private val observableSchedules = MutableLiveData<List<Schedule>>().apply {
        value = listOf()
    }

    private var shouldReturnError = false
    private var exceptionMessage = ""

    // Faked remote data
    private var remoteEventList = listOf<Event>()
    private var remoteScheduleList = listOf<Schedule>()

    override fun observeEvents(): LiveData<List<Event>> {
        return observableEvents
    }

    override fun observeSchedule(): LiveData<List<Schedule>> {
        return observableSchedules
    }

    override suspend fun getEvents(): List<Event> {
        return observableEvents.value!!
    }

    override suspend fun getSchedule(): List<Schedule> {
        return observableSchedules.value!!
    }

    override suspend fun refreshEvents() {
        if (shouldReturnError) {
            throw IOException(exceptionMessage)
        }
        observableEvents.value = remoteEventList
    }

    override suspend fun refreshSchedule() {
        if (shouldReturnError) {
            throw IOException(exceptionMessage)
        }
        observableSchedules.value = remoteScheduleList
    }

    fun setReturnError(value: Boolean, errorMessage: String) {
        shouldReturnError = value
        exceptionMessage = errorMessage
    }

    /***
     * Submit a list of events as a faked remote data source.
     * When refreshEvents() is called, the list will be loaded to the ViewModel
     */
    fun submitEventList(events: List<Event>) {
        remoteEventList = events
    }

    /***
     * Submit a list of events as a faked remote data source.
     * When refreshEvents() is called, the list will be loaded to the ViewModel
     */
    fun submitScheduleList(schedules: List<Schedule>) {
        remoteScheduleList = schedules
    }

}