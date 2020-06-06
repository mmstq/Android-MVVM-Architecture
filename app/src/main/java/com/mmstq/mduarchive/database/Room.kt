package com.mmstq.mduarchive.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoticeDao{

    @Query("select * from notice where source like :from order by `index` ASC")
    fun getNotice(from:String): LiveData<List<NoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( noticeDB: List<NoticeEntity>)
}


@Database(entities = [NoticeEntity::class], version = 1)
abstract class NoticeDatabase: RoomDatabase() {
    abstract val noticeDao: NoticeDao
}

private lateinit var INSTANCE: NoticeDatabase

fun getDatabase(context: Context): NoticeDatabase {
    synchronized(NoticeDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                NoticeDatabase::class.java,
                "notice").build()
        }
    }
    return INSTANCE
}