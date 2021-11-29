/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.data.source.remote

/**
 * Remote Data Source interface
 *
 * A Remote Data Source:
 * - Works like a black box
 * - Has its own ways to retrieve/generate the data
 * - Has its own choices to/not to cache any data
 * - Can only provide static copies of data (i.e. no LiveData)
 */
abstract class BaseRemoteDataSource {

    // Return static data
    abstract suspend fun getEvents(): ApiResult<Any>
    abstract suspend fun getSchedules(): ApiResult<Any>
}