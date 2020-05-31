package com.bogiruapps.rdshapp.notice.notice_edit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        configureBottomNavigation()

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
        noticeEditViewModel.openEditNotice.observe(viewLifecycleOwner, EventObserver {
            showEditNotice()
        })

        noticeEditViewModel.closeEditNotice.observe(viewLifecycleOwner, EventObserver {
            hideEditNotice()
        })

        noticeEditViewModel.openNoticeFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_noticeEditFragment_to_noticeFragment)
        })

        noticeEditViewModel.showSnackbar.observe(viewLifecycleOwner, Observer {
            showSnackbar(it)
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)


        if (noticeEditViewModel.checkCreateNoticeStatus()) {
            activity?.main_toolbar?.title = "Создание объявления"
        } else  {
            activity?.main_toolbar?.title = "Редактирование объявления"
        }

        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun configureBottomNavigation() {
        activity!!.bottomNavigationView.visibility = View.GONE
    }

    private fun showEditNotice() {
        binding.noticeEditLayout.visibility = View.VISIBLE
    }

    private fun hideEditNotice() {
        binding.noticeEditLayout.visibility = View.INVISIBLE
    }




}
