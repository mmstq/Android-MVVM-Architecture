package com.mmstq.mduarchive.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mmstq.mduarchive.model.Notice

@Entity(tableName = "notice")
data class NoticeEntity constructor(
    @PrimaryKey
    @NonNull
    var link: String = "",
    var title: String = "",
    var date: String = "",
    var source: String = ""
)


fun List<NoticeEntity>.asDomainModel(): List<Notice> {
    return map {
        Notice(
            title = it.title,
            date = it.date,
            link = it.link)
    }
}



