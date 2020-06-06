package com.mmstq.mduarchive.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.andrognito.flashbar.Flashbar
import com.mmstq.mduarchive.R
import com.mmstq.mduarchive.adapter.ArchiveAdapter
import com.mmstq.mduarchive.adapter.NoticeListener
import com.mmstq.mduarchive.databinding.FragmentMduBinding
import com.mmstq.mduarchive.utility.Util
import com.mmstq.mduarchive.viewModel.MDUViewModel



class MDU : Fragment() {

    private lateinit var binding: FragmentMduBinding
    private var adapter: ArchiveAdapter? = null
    private val snackBar: Flashbar by lazy {
        Util.getSnackBar(activity!!, context!!).build()
    }
    private val snackBarError: Flashbar by lazy {
        Util.getSnackBar(activity!!, context!!).title("Unknown network error").positiveActionText("retry").positiveActionTapListener(object : Flashbar.OnActionTapListener{
            override fun onActionTapped(bar: Flashbar) {
                bar.dismiss()
                viewModel.refreshDataFromRepository()
            }
        }).negativeActionText("hide").negativeActionTapListener(object : Flashbar.OnActionTapListener{
            override fun onActionTapped(bar: Flashbar) {
                bar.dismiss()
            }
        }).build()
    }

    private val viewModel: MDUViewModel by lazy {
        val activity = requireNotNull(this.activity) { "Not allowed" }
        ViewModelProvider(
            this,
            MDUViewModel.Factory(activity.application)
        ).get(MDUViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mdu, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = ArchiveAdapter(NoticeListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "No application found", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.notice.observe(viewLifecycleOwner, Observer {
            it?.apply {
                adapter?.submitList(it)
            }
        })


        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer {

            if (it) {
                snackBar.dismiss()
                snackBarError.show()
            }else{
                snackBarError.dismiss()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                showSnacks()
                binding.refresh.isRefreshing = false

            } else {
                snackBar.dismiss()
            }
        })

        binding.refresh.setOnRefreshListener {
            viewModel.refreshDataFromRepository()
        }

        binding.archiveList.adapter = adapter
        binding.executePendingBindings()
        return binding.root
    }
    private fun showSnacks(){
        Handler().postDelayed({ //
            snackBar.show()
        }, 400)
    }
}
