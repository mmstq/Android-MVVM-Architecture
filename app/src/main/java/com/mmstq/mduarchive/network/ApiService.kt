package com.mmstq.mduarchive.network

import com.mmstq.mduarchive.model.Items
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(value = "notice/notice")
    fun getNoticesAsync(@Query("from") from: String): Deferred<Items>
}


