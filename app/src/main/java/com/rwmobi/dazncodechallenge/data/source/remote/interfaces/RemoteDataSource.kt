/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.remote.interfaces

import com.rwmobi.dazncodechallenge.data.source.remote.ApiResult

/**
 * Remote Data Source interface
 *
 * A Remote Data Source:
 * - Works like a black box
 * - Has its own ways to retrieve/generate the data
 * - Has its own choices to/not to cache any data
 * - Can only provide static copies of data (i.e. no LiveData)
 */
interface RemoteDataSource {
    // Return static data
    suspend fun getEvents(): ApiResult<Any>

    suspend fun getSchedules(): ApiResult<Any>
}
