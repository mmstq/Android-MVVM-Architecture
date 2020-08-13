package com.mmstq.mduarchive.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class HomeViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    val liveData = MutableLiveData<Boolean>(false)
    fun onClickGo() {
        liveData.value = true
    }
}