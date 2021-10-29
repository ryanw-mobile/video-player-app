/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.ryanwong.dazn.codechallenge.base.BaseRemoteDataSource
import uk.ryanwong.dazn.codechallenge.data.source.remote.entities.asDomainModel
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule
import java.util.*

private const val BASE_URL = "https://us-central1-dazn-sandbox.cloudfunctions.net/"

class SandBoxAPIDataSource(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    BaseRemoteDataSource() {
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

    /***
     * Retrieve events from network
     */
    override suspend fun getEvents(): ApiResult<List<Event>> = withContext(ioDispatcher) {
        return@withContext try {
            // We could add sorting here, but it is redundant as our local data source will handle that
            ApiResult.Success(retrofitService.getEvents().asDomainModel())  // .sortedBy { it.date }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    /***
     * Retrieve events from network
     */
    override suspend fun getSchedules(): ApiResult<List<Schedule>> = withContext(ioDispatcher) {
        return@withContext try {
            // We could add sorting here, but it is redundant as our local data source will handle that
            ApiResult.Success(
                retrofitService.getSchedule().asDomainModel()
            )  // .sortedBy { it.date }
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}