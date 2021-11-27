/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.ryanwong.dazn.codechallenge.data.source.remote.DaznApiService
import java.util.*
import javax.inject.Singleton

private const val BASE_URL = "https://us-central1-dazn-sandbox.cloudfunctions.net/"

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModules {

    /**
     * Moshi's composition mechanism tries to find the best adapter for each type.
     * It starts with the first adapter or factory registered with Moshi.Builder.add(),
     * and proceeds until it finds an adapter for the target type.
     * If a type can be matched multiple adapters, the earliest one wins.
     * To register an adapter at the end of the list, use Moshi.Builder.addLast() instead.
     * This is most useful when registering general-purpose adapters, such as the KotlinJsonAdapterFactory below.
     */
    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    @Provides
    @Singleton
    fun retrofitService(retrofit: Retrofit): DaznApiService =
        retrofit.create(DaznApiService::class.java)
}