package com.mmstq.mduarchive.di

import com.mmstq.mduarchive.network.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class NoticeApiModule {


    companion object{

        @Provides
        fun provideNoticeApi(retrofit: Retrofit):ApiService{
            return retrofit.create(ApiService::class.java )
        }


    }

}