package com.mmstq.mduarchive.utility

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mmstq.mduarchive.model.Notice

@BindingAdapter("titleString")
fun TextView.setTitleString(item: Notice){
    text = item.title
}

@BindingAdapter("dateString")
fun TextView.setDateString(item: Notice){
    text = item.date
}

/*
@BindingAdapter("adapter")
fun RecyclerView.setAdapter(it:ArchiveAdapter){
    adapter=it
}
*/


