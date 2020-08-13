package com.mmstq.mduarchive.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.andrognito.flashbar.Flashbar
import com.mmstq.mduarchive.R
import com.mmstq.mduarchive.adapter.ArchiveAdapter
import com.mmstq.mduarchive.adapter.NoticeListener
import com.mmstq.mduarchive.databinding.FragmentUietBinding
import com.mmstq.mduarchive.model.Notice
import com.mmstq.mduarchive.utility.Util
import com.mmstq.mduarchive.viewModel.UIETViewModel
import com.mmstq.mduarchive.viewModel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import javax.inject.Inject

class UIET : DaggerFragment() {

    private var adapter: ArchiveAdapter? = null

    @Inject
    lateinit var viewModel:UIETViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private val snackBar: Flashbar by lazy {
        Util.getSnackBar(activity!!, context!!).build()
    }
    private val snackBarError: Flashbar by lazy {
        Util.getSnackBar(activity!!, context!!).title("Unknown network error").positiveActionText("retry").positiveActionTapListener(object : Flashbar.OnActionTapListener{
            override fun onActionTapped(bar: Flashbar) {
                bar.dismiss()
                viewModel.refresh()
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

        viewModel = viewModelFactory.create(viewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        adapter =
            ArchiveAdapter(NoticeListener { _:View, notice: Notice ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(notice.link))
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

//        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer {
//
//            if (it) {
//                snackBar.dismiss()
//                snackBarError.show()
//            }else{
//                snackBarError.dismiss()
//            }
//        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                showSnacks()
                binding.refresh.isRefreshing = false

            } else {
                snackBar.dismiss()
            }
        })
        binding.refresh.setOnRefreshListener {
            viewModel.refresh()
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