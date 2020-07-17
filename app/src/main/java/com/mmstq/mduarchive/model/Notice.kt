package com.mmstq.mduarchive.model

import com.mmstq.mduarchive.database.NoticeEntity
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Notice (
    var title: String = "",
    var date: String = "",
    var link: String = ""
)
fun List<Notice>.asDatabaseModel(): List<NoticeEntity> {
    return map {
        NoticeEntity(
            title = it.title,
            date = it.date,
            link = it.link)
    }
}

data class Items(
    var items:List<Notice>
)

