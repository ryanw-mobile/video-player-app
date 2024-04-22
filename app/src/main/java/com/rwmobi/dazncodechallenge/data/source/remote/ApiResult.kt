/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.remote

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ApiResult<out R> {
    data class Success<out T>(val data: T) : ApiResult<T>()

    data class Error(val exception: Exception) : ApiResult<Nothing>()

    object Loading : ApiResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if Result is of type Success & holds non-null Success.data.
 */
val ApiResult<*>.succeeded
    get() = this is ApiResult.Success && data != null
