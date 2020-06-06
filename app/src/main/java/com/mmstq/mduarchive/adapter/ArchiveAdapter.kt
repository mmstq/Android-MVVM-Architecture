package com.mmstq.mduarchive.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mmstq.mduarchive.databinding.TextViewItemBinding
import com.mmstq.mduarchive.model.Notice



class ArchiveAdapter(private val clickListener: NoticeListener) : ListAdapter<Notice, ArchiveAdapter.ViewHolder>(
    NoticeDiffCallBack()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }


    class ViewHolder private constructor(private val binding: TextViewItemBinding): RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TextViewItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }

        fun bind(
            item: Notice,
            clickListener: NoticeListener
        ) {
            binding.notice = item
            binding.listener = clickListener
            binding.executePendingBindings()
        }

    }
}

class NoticeDiffCallBack: DiffUtil.ItemCallback<Notice>(){
    override fun areItemsTheSame(oldItem: Notice, newItem: Notice): Boolean {
        return oldItem.title==newItem.title
    }

    override fun areContentsTheSame(oldItem: Notice, newItem: Notice): Boolean {
        return oldItem == newItem
    }

}

class NoticeListener(val clickListener: (notice:Notice)->Unit){
    fun onClick(notice:Notice)= clickListener(notice)
}

