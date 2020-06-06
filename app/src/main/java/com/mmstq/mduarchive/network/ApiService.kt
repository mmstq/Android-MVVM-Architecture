package com.mmstq.mduarchive.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mmstq.mduarchive.model.Items
import com.mmstq.mduarchive.model.Notice
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import javax.inject.Singleton

interface ApiService{
    @GET(value = "notice?from=mdu")
    fun getNoticesFromMDUAsync(): Deferred<Items>

    @GET(value = "notice?from=uiet")
    fun getNoticesFromUIETAsync(): Deferred<Items>
}

object ApiNetwork{
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://zuko.eu-gb.mybluemix.net/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val api: ApiService by lazy {
        retrofit.create(
            ApiService::class.java)
    }

}

@Module
abstract class NetworkModule{

    @Provides
    @Singleton
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder()
            .build()
    }
}

