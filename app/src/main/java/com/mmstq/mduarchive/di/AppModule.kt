package com.mmstq.mduarchive.di

import android.app.Application
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mmstq.mduarchive.database.NoticeDatabase
import com.mmstq.mduarchive.network.ApiService
import com.mmstq.mduarchive.network.Repository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class AppModule {


    companion object {


        @Provides
        @Singleton
        fun getRetrofitInstance():Retrofit{
            return Retrofit.Builder()
                .baseUrl("https://zuko.eu-gb.mybluemix.net/")
                .addConverterFactory(MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                ))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        }

        @Singleton
        @Provides
        fun provideDatabase(application: Application): NoticeDatabase {
            return Room.databaseBuilder(application,
                NoticeDatabase::class.java,
                "notice").build()
        }

        @Provides
        @Singleton
        fun provideNoticeApi(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java )
        }

    }
}