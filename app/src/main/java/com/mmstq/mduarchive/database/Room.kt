package com.mmstq.mduarchive.database

import androidx.lifecycle.LiveData
import androidx.room.*
import dagger.Module
import javax.inject.Singleton

@Dao
interface NoticeDao {

    @Query("select * from notice where source like :from limit 50")
    fun getNotice(from: String): LiveData<List<NoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(noticeDB: List<NoticeEntity>)
}

@Module
@Database(entities = [NoticeEntity::class], version = 1)
abstract class NoticeDatabase : RoomDatabase() {
    abstract val noticeDao: NoticeDao
}