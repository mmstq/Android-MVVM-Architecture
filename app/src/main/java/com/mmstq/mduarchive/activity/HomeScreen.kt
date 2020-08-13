package com.mmstq.mduarchive.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.google.firebase.messaging.FirebaseMessaging
import com.mmstq.mduarchive.databinding.HomeScreenBinding
import com.mmstq.mduarchive.viewModel.HomeViewModel
import com.mmstq.mduarchive.viewModel.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class HomeScreen : DaggerAppCompatActivity() {

    private lateinit var binding: HomeScreenBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = this.getSharedPreferences("config", 0)
        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel = viewModelFactory.create(viewModel::class.java)

        binding.viewModel = viewModel

        viewModel.liveData.observe(this, Observer {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        })
        binding.subscribe.isChecked = sharedPreferences.getBoolean("subscribed", false)

        binding.subscribe.setOnCheckedChangeListener { _, checked ->

            Log.d("isChecked", checked.toString())

            if (checked) {
                FirebaseMessaging.getInstance().subscribeToTopic("mdu")
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("mdu")
            }
            sharedPreferences.edit().putBoolean("subscribed", checked).apply()
        }

    }
}