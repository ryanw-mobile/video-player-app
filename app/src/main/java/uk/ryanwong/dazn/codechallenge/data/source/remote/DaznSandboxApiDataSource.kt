/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import uk.ryanwong.dazn.codechallenge.data.source.DaznApiDataSource
import java.util.*

private const val BASE_URL = "https://us-central1-dazn-sandbox.cloudfunctions.net/"

class DaznSandboxApiDataSource(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    DaznApiDataSource {
    /**
     * Moshi's composition mechanism tries to find the best adapter for each type.
     * It starts with the first adapter or factory registered with Moshi.Builder.add(),
     * and proceeds until it finds an adapter for the target type.
     * If a type can be matched multiple adapters, the earliest one wins.
     * To register an adapter at the end of the list, use Moshi.Builder.addLast() instead.
     * This is most useful when registering general-purpose adapters, such as the KotlinJsonAdapterFactory below.
     */
    private val moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: DaznApiService by lazy {
        retrofit.create(DaznApiService::class.java)
    }

    private var _events = MutableLiveData<ApiResult<List<Event>>>()
    private var _schedule = MutableLiveData<ApiResult<List<Schedule>>>()

    init {
        _events.value = ApiResult.Success(emptyList())
        _schedule.value = ApiResult.Success(emptyList())
    }

    override fun observeEvents(): LiveData<ApiResult<List<Event>>> {
        return _events
    }

    override fun observeSchedule(): LiveData<ApiResult<List<Schedule>>> {
        return _schedule
    }

    override suspend fun getEvents(): ApiResult<List<Event>> = withContext(ioDispatcher) {
        return@withContext try {
            // We could add sorting here if we intend to show network data without using DB
            ApiResult.Success(retrofitService.getEvents())  // .sortedBy { it.date }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    override suspend fun getSchedules(): ApiResult<List<Schedule>> = withContext(ioDispatcher) {
        return@withContext try {
            // We could add sorting here if we intend to show network data without using DB
            ApiResult.Success(retrofitService.getSchedule())  // .sortedBy { it.date }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    // Implementation note:
    // While this data source is intended to be cached using a local datasource before use,
    // it can also be used standalone without database caching.
    // In this case, this data source keeps an internal data structure
    // to hold the results returned by the API, and currently without sorting.
    override suspend fun syncEvents(events: List<Event>) {
        _events.value = getEvents()!!  // redundant !! added to avoid compiler error
    }

    override suspend fun syncSchedule(schedules: List<Schedule>) {
        _schedule.value = getSchedules()!!  // redundant !! added to avoid compiler error
    }
}