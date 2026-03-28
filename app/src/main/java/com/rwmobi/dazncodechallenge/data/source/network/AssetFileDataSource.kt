/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.data.source.network

import android.content.Context
import com.rwmobi.dazncodechallenge.data.source.network.dto.EventNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.dto.ScheduleNetworkDto
import com.rwmobi.dazncodechallenge.data.source.network.interfaces.NetworkDataSource
import com.rwmobi.dazncodechallenge.di.DispatcherModule
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AssetFileDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : NetworkDataSource {

    override suspend fun getEvents(): List<EventNetworkDto> = withContext(dispatcher) {
        val json = context.assets.open("getEvents.json").bufferedReader().use { it.readText() }
        val type = Types.newParameterizedType(List::class.java, EventNetworkDto::class.java)
        moshi.adapter<List<EventNetworkDto>>(type).fromJson(json) ?: emptyList()
    }

    override suspend fun getSchedules(): List<ScheduleNetworkDto> = withContext(dispatcher) {
        val json = context.assets.open("getSchedule.json").bufferedReader().use { it.readText() }
        val type = Types.newParameterizedType(List::class.java, ScheduleNetworkDto::class.java)
        moshi.adapter<List<ScheduleNetworkDto>>(type).fromJson(json) ?: emptyList()
    }
}
