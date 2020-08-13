package com.mmstq.mduarchive.adapter

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mmstq.mduarchive.databinding.TextViewItemBinding
import com.mmstq.mduarchive.model.Notice
import com.mmstq.mduarchive.utility.Util.Companion.pressAnimation
import java.util.concurrent.TimeUnit


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

        private val time= System.currentTimeMillis()

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

            Log.d("days","${item.date} ${TimeUnit.MILLISECONDS.toDays(time).minus(TimeUnit.SECONDS.toDays(item.storedOn.toLong()))}")

            if (TimeUnit.MILLISECONDS.toDays(time).minus(TimeUnit.SECONDS.toDays(item.storedOn.toLong())) <= 2){
                binding.imageView.visibility = View.VISIBLE
            }else{
                binding.imageView.visibility = View.GONE
            }
            
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

class NoticeListener(val clickListener: (view:View, notice:Notice)->Unit){
    fun onClick(view:View, notice:Notice){
        pressAnimation(view=view)
        Handler().postDelayed({
            clickListener(view, notice)
        },200)
    }
}

