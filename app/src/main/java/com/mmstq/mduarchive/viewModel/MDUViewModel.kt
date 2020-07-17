package com.mmstq.mduarchive.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.mmstq.mduarchive.network.Repository
import com.mmstq.mduarchive.database.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.Exception

class MDUViewModel(application: Application): AndroidViewModel(application) {
    private val repository =
        Repository(getDatabase(application))
    val notice = repository.noticesFromMDU

    private val job = SupervisorJob()
    private val viewModelScope = CoroutineScope(job+Dispatchers.Main)

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    //error handler
    private val _eventNetworkError = MutableLiveData(false)
    val eventNetworkError:LiveData<Boolean>
        get() = _eventNetworkError

    private var _networkError:String? = ""
    val networkError:String?
        get() = _networkError


    init {
        refreshDataFromRepository()
    }

    fun refreshDataFromRepository(){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.refreshNotices("mdu")
                _isLoading.value = false
                _eventNetworkError.value = false
            }catch (e:Exception){
                Log.d("error", "i ran")
                _isLoading.value = false
                Log.d("errorLoading", "message ${e.message} lmessage:${e.localizedMessage} stacktrace:${e.stackTrace}", e.cause)
                e.printStackTrace()
                if(notice.value.isNullOrEmpty()){
                    _eventNetworkError.value = true
                    _networkError = e.localizedMessage
                }
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MDUViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MDUViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}