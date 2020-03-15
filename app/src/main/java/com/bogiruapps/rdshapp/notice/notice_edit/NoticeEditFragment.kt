package com.bogiruapps.rdshapp.notice.notice_edit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentNoticeEditBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
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
        configureToolbar()
        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice_edit, container, false)
        binding.viewModel = noticeEditViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupObserverViewModel() {
        noticeEditViewModel.openEditNotice.observe(this, EventObserver {
            showEditNotice()
        })

        noticeEditViewModel.closeEditNotice.observe(this, EventObserver {
            hideEditNotice()
        })

        noticeEditViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            findNavController().navigate(R.id.action_noticeEditFragment_to_noticeFragment)
        })

        noticeEditViewModel.showSnackbar.observe(this, Observer {
            showSnackbar(it)
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)
        val image = activity!!.headerImage

        activity?.window?.decorView?.systemUiVisibility = View.VISIBLE

        if (noticeEditViewModel.checkCreateNoticeStatus()) {
            activity?.collapseToolbar?.title = "Создание объявления"
        } else  {
            activity?.collapseToolbar?.title = "Редактирование объявления"
        }

        activity?.appBar?.setExpanded(false)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }



    private fun showEditNotice() {
        binding.editNoticeLayout.visibility = View.VISIBLE
    }

    private fun hideEditNotice() {
        binding.editNoticeLayout.visibility = View.INVISIBLE
    }




}
