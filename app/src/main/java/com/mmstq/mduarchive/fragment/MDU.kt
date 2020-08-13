package com.mmstq.mduarchive.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.flashbar.Flashbar
import com.mmstq.mduarchive.R
import com.mmstq.mduarchive.adapter.ArchiveAdapter
import com.mmstq.mduarchive.adapter.NoticeListener
import com.mmstq.mduarchive.databinding.FragmentMduBinding
import com.mmstq.mduarchive.model.Notice
import com.mmstq.mduarchive.utility.Util
import com.mmstq.mduarchive.viewModel.MDUViewModel
import com.mmstq.mduarchive.viewModel.UIETViewModel
import com.mmstq.mduarchive.viewModel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MDU : DaggerFragment() {

    private lateinit var binding: FragmentMduBinding
    private var adapter: ArchiveAdapter? = null
    private val snackBar: Flashbar by lazy {
        Util.getSnackBar(activity!!, context!!).build()
    }
    private val snackBarError: Flashbar by lazy {
        Util.getSnackBar(activity!!, context!!).title("Unknown network error")
            .positiveActionText("retry")
            .positiveActionTapListener(object : Flashbar.OnActionTapListener {
                override fun onActionTapped(bar: Flashbar) {
                    bar.dismiss()
                    viewModel.refreshDataFromRepository()
                }
            }).negativeActionText("hide")
            .negativeActionTapListener(object : Flashbar.OnActionTapListener {
                override fun onActionTapped(bar: Flashbar) {
                    bar.dismiss()
                }
            }).build()
    }

    @Inject
    lateinit var viewModel:MDUViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mdu, container, false)
        viewModel = viewModelFactory.create(viewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = ArchiveAdapter(NoticeListener { _: View, notice: Notice ->
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(notice.link))
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
                dismissSnacks()
                snackBarError.show()
            } else {
                snackBarError.dismiss()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            Log.d("isLoading", it.toString())

            if (it) {
                showSnacks()
                binding.refresh.isRefreshing = false

            } else {
                dismissSnacks()
            }
        })

        binding.refresh.setOnRefreshListener {
            viewModel.refreshDataFromRepository()
        }

        binding.archiveList.adapter = adapter
        binding.executePendingBindings()
        adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.archiveList.scrollToPosition(0)

            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.archiveList.scrollToPosition(0)

            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                binding.archiveList.scrollToPosition(0)
            }

        })
        return binding.root
    }


    private fun showSnacks() {
        snackBar.show()
    }

    private fun dismissSnacks(){
        Handler().postDelayed({
            snackBar.dismiss()
        },700)
    }
}
