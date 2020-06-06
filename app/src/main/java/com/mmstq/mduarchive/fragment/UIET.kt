package com.mmstq.mduarchive.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.andrognito.flashbar.Flashbar

import com.mmstq.mduarchive.R
import com.mmstq.mduarchive.adapter.ArchiveAdapter
import com.mmstq.mduarchive.adapter.NoticeListener
import com.mmstq.mduarchive.databinding.FragmentUietBinding
import com.mmstq.mduarchive.utility.Util
import com.mmstq.mduarchive.viewModel.UIETViewModel
import java.lang.Exception

class UIET : Fragment() {

    private var adapter: ArchiveAdapter? = null

    private val viewModel:UIETViewModel by lazy {
        val activity = requireNotNull(this.activity){"Not allowed"}
        ViewModelProvider(this, UIETViewModel.Factory(activity.application)).get(UIETViewModel::class.java)
    }

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentUietBinding>(inflater, R.layout.fragment_uiet, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        adapter =
            ArchiveAdapter(NoticeListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "No application found", Toast.LENGTH_LONG).show()
                }
            })
        viewModel.notices.observe(viewLifecycleOwner, Observer{
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
        }, 200)
    }

}