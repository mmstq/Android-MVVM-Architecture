package com.mmstq.mduarchive.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mmstq.mduarchive.fragment.MDU
import com.mmstq.mduarchive.fragment.UIET

class PagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0-> UIET()
            else-> MDU()
        }
    }

}