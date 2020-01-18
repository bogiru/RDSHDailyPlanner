package com.bogiruapps.rdshapp.notice.notice_edit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentNoticeEditBinding
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class NoticeEditFragment : Fragment() {

    private val noticeEditViewModel: NoticeEditViewModel by viewModel()
    private lateinit var binding: FragmentNoticeEditBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice_edit, container, false)
        binding.viewModel = noticeEditViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        noticeEditViewModel.openEditNotice.observe(this, EventObserver {
            showEditNotice()
        })

        noticeEditViewModel.closeEditNotice.observe(this, EventObserver {
            hideEditNotice()
        })

        noticeEditViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            findNavController().navigate(R.id.action_noticeDetailFragment_to_noticeFragment)
        })

    }

    private fun showEditNotice() {
        binding.editNoticeLayout.visibility = View.VISIBLE
    }

    private fun hideEditNotice() {
        binding.editNoticeLayout.visibility = View.INVISIBLE
    }




}
