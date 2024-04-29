/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.dazncodechallenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatcherModule::class],
)
object TestDispatcherModule {
    @DispatcherModule.DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()

    @DispatcherModule.IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()

    @DispatcherModule.MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()
}
