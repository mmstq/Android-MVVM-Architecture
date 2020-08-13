package com.mmstq.mduarchive.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mmstq.mduarchive.database.NoticeDatabase
import com.mmstq.mduarchive.database.NoticeEntity
import com.mmstq.mduarchive.database.asDomainModel
import com.mmstq.mduarchive.model.Notice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(val database:NoticeDatabase, private val apiService: ApiService) {

    val noticesFromUIET : LiveData<List<Notice>> = convertToDomainModel("uiet")
    val noticesFromMDU : LiveData<List<Notice>> = convertToDomainModel("mdu")

    private fun convertToDomainModel(from:String):LiveData<List<Notice>>{
        return Transformations.map(database.noticeDao.getNotice(from)){
            it.asDomainModel()
        }
    }

    suspend fun refreshNotices(from:String) {

        withContext(Dispatchers.IO){

            val notices = apiService.getNoticesAsync(from).await()

            database.noticeDao.insertAll(notices.items.map { NoticeEntity(
                title = it.title,
                date = it.date,
                link = it.link,
                source = from,
                storedOn = it.storedOn
            ) })
        }
    }

}