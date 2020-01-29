package com.bogiruapps.rdshapp.notice.notice_detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentNoticeDetailBinding
import com.bogiruapps.rdshapp.databinding.FragmentNoticeEditBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class NoticeDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoticeDetailBinding

    val noticeDetailViewModel: NoticeDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        configureToolbar()
        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice_detail, container, false)
        binding.viewModel = noticeDetailViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupObserverViewModel() {

    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)

        activity?.toolbar?.menu?.findItem(R.id.item_share)?.isVisible = false
        editItem?.isVisible = true
        deleteItem?.isVisible = true

        editItem?.setOnMenuItemClickListener {
            findNavController().navigate(R.id.action_noticeDetailFragment_to_noticeEditFragment)
            return@setOnMenuItemClickListener true
        }

    }
}
