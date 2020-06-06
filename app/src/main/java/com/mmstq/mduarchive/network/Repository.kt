package com.mmstq.mduarchive.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mmstq.mduarchive.database.NoticeDatabase
import com.mmstq.mduarchive.database.NoticeEntity
import com.mmstq.mduarchive.database.asDomainModel
import com.mmstq.mduarchive.model.Items
import com.mmstq.mduarchive.model.Notice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val database:NoticeDatabase) {

    val noticesFromUIET : LiveData<List<Notice>> = Transformations.map(database.noticeDao.getNotice("uiet")){
        it.asDomainModel()
    }
    val noticesFromMDU : LiveData<List<Notice>> = Transformations.map(database.noticeDao.getNotice("mdu")){
        it.asDomainModel()
    }

    suspend fun refreshNotices(from:String) {
        withContext(Dispatchers.IO){

            val notices = if(from == "uiet") {
                ApiNetwork.api.getNoticesFromUIETAsync().await()
            }else{
                ApiNetwork.api.getNoticesFromMDUAsync().await()
            }

            print(notices.items)
            database.noticeDao.insertAll(notices.items.map { NoticeEntity(
                title = it.title,
                date = it.date,
                index = it.index,
                link = it.link,
                source = from
            ) })
        }
    }

}