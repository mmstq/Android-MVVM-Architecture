package com.mmstq.mduarchive.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mmstq.mduarchive.network.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.CancellationException
import javax.inject.Inject

class UIETViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val notices = repository.noticesFromUIET

    //coroutines
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    // error show
    private fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                setLoading(true)
                repository.refreshNotices("uiet")
                setLoading(false)

            } catch (exception: Exception) {
                setLoading(false)
                Timber.e(exception.message!!)
            }
        }
    }

    private fun handleFailure(throwable: Throwable) {
        if (throwable is CancellationException) return

        Timber.e(throwable)
        setLoading(isLoading = false)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}