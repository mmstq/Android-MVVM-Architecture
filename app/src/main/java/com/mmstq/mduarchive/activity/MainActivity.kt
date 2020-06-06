package com.mmstq.mduarchive.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.mmstq.mduarchive.R
import com.mmstq.mduarchive.adapter.PagerAdapter
import com.mmstq.mduarchive.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()

    }


    private fun init(){
        setSupportActionBar(binding.toolbar)
        binding.viewpager.adapter = PagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewpager, false){ tab, position->
            tab.text = when (position){
                0->"UIET Archives"
                else->"MDU Archives"
            }

        }.attach()
    }
}
