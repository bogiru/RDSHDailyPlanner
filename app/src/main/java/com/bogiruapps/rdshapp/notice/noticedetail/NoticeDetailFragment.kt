package com.bogiruapps.rdshapp.notice.noticedetail


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentNoticeDetailBinding
import com.bogiruapps.rdshapp.utils.showSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class NoticeDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoticeDetailBinding

    private val noticeDetailViewModel: NoticeDetailViewModel by viewModel()

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
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        noticeDetailViewModel.openNoticeFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            showNoticeFragment()
        })

        noticeDetailViewModel.openNoticeEditFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            showNoticeEditFragment()
        })

        noticeDetailViewModel.openDialogDeleteNoticeEvent.observe(viewLifecycleOwner, EventObserver {
            showAlertDialogDelete()
        })

        noticeDetailViewModel.showSnackbar.observe(viewLifecycleOwner, Observer {message ->
            showSnackbar(view!!, message)
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)

        editItem?.setOnMenuItemClickListener {
            noticeDetailViewModel.showEditNoticeFragment()
            return@setOnMenuItemClickListener true
        }

        deleteItem?.setOnMenuItemClickListener {
            noticeDetailViewModel.showDeleteNoticeFragment()
            return@setOnMenuItemClickListener true
        }

        editItem?.isVisible = true
        deleteItem?.isVisible = true
    }

    @SuppressLint("ResourceType")
    private fun showAlertDialogDelete(){
        val alertBuilder = MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
        alertBuilder.apply {
            setTitle("Удалить объявление")
            setMessage("Вы уверены, что хотите удалить объявление?")
            setIconAttribute(R.drawable.rdsh_image)
            setCancelable(true)
            setPositiveButton(
                "Да"
            ) { _: DialogInterface, _: Int ->
                noticeDetailViewModel.deleteNotice()
                showNoticeFragment()
            }
            setNegativeButton(
                "Нет"
            ) { _: DialogInterface, _: Int ->
            }
            show()
        }
    }

    private fun showNoticeFragment() {
        findNavController().navigate(R.id.action_noticeDetailFragment_to_noticeFragment)
    }

    private fun showNoticeEditFragment() {
        findNavController().navigate(R.id.action_noticeDetailFragment_to_noticeEditFragment)
    }

}
