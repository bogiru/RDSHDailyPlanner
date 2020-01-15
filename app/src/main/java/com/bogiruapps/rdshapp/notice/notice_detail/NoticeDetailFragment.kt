package com.bogiruapps.rdshapp.notice.notice_detail


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.Event
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentNoticeDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class NoticeDetailFragment : Fragment() {

    private val noticeDetailViewModel: NoticeDetailViewModel by viewModel()
    private lateinit var binding: FragmentNoticeDetailBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice_detail, container, false)
        binding.viewModel = noticeDetailViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        noticeDetailViewModel.openEditNotice.observe(this, EventObserver {
            showEditNotice()
        })

        noticeDetailViewModel.closeEditNotice.observe(this, EventObserver {
            hideEditNotice()
        })

        noticeDetailViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            findNavController().navigate(R.id.noticeFragment)
        })

    }

    private fun showEditNotice() {
        binding.editNoticeLayout.visibility = View.VISIBLE
    }

    private fun hideEditNotice() {
        binding.editNoticeLayout.visibility = View.INVISIBLE
    }




}
