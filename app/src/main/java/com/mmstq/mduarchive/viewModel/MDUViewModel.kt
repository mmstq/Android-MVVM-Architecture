package com.mmstq.mduarchive.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mmstq.mduarchive.network.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class MDUViewModel @Inject constructor(application: Application, private val repository: Repository) : AndroidViewModel(application) {

    val notice = repository.noticesFromMDU

    private val job = SupervisorJob()
    private val viewModelScope = CoroutineScope(job + Dispatchers.Main)

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    //error handler
    private val _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _networkError: String? = ""
    val networkError: String?
        get() = _networkError


    init {
        refreshDataFromRepository()
    }

    fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.refreshNotices("mdu")
                _isLoading.value = false
                _eventNetworkError.value = false
            } catch (e: Exception) {
                Log.d("error", "i ran")
                _isLoading.value = false
                Log.d(
                    "errorLoading",
                    "message ${e.message} lmessage:${e.localizedMessage} stacktrace:${e.stackTrace}",
                    e.cause
                )
                e.printStackTrace()
                if (notice.value.isNullOrEmpty()) {
                    _eventNetworkError.value = true
                    _networkError = e.localizedMessage
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}