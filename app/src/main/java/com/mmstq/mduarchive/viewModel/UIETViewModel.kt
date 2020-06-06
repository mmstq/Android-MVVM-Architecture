package com.mmstq.mduarchive.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.mmstq.mduarchive.network.Repository
import com.mmstq.mduarchive.database.getDatabase
import kotlinx.coroutines.*
import java.io.IOException

class UIETViewModel(application: Application) : AndroidViewModel(application){

    private val repository =
        Repository(getDatabase(application))
    val notices = repository.noticesFromUIET

    //coroutines
    private val viewModelJob  = Job()
    private val viewModelScope = CoroutineScope(viewModelJob+Dispatchers.Main)


    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    //error check
    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError:LiveData<Boolean>
        get() = _eventNetworkError

    // error show

    private val _networkError = MutableLiveData<String>()
    val networkError:LiveData<String>
        get() = _networkError



    init {
        refreshDataFromRepository()
    }

    fun refreshDataFromRepository(){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.refreshNotices("uiet")
                _isLoading.value = false
                _eventNetworkError.value = false

            }catch (exception:IOException){
                _isLoading.value = false
                exception.printStackTrace()
                if(notices.value.isNullOrEmpty()){
                    _eventNetworkError.value = true
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UIETViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UIETViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }

}