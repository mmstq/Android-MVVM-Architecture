package com.mmstq.mduarchive.network

import androidx.annotation.StringRes
import com.mmstq.mduarchive.R

enum class ErrorHandler(@StringRes private val resourceId:Int): ErrorEvent{
    
            NONE(0),
            NO_INTERNET(R.string.app_name);

    override fun getErrorResource()=resourceId
}

interface ErrorEvent{
    @StringRes
    fun getErrorResource():Int
}