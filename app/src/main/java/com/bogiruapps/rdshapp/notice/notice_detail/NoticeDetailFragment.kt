package com.bogiruapps.rdshapp.notice.notice_detail


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentNoticeDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice_detail, container, false)
        binding.viewModel = noticeDetailViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        noticeDetailViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            findNavController().navigate(R.id.action_noticeDetailFragment_to_noticeFragment)
        })

        noticeDetailViewModel.openNoticeEditFragmentEvent.observe(this, EventObserver {
            findNavController().navigate(R.id.action_noticeDetailFragment_to_noticeEditFragment)
        })

        noticeDetailViewModel.openNoticeDeleteFragmentEvent.observe(this, EventObserver {
            showAllertDialogDelete()
        })

        noticeDetailViewModel.showToast.observe(this, EventObserver {
            showSnackBar(it)
        })

    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)
        val image = activity!!.headerImage

        editItem?.setOnMenuItemClickListener {
            noticeDetailViewModel.showEditNoticeFragment()
            return@setOnMenuItemClickListener true
        }

        deleteItem?.setOnMenuItemClickListener {
            noticeDetailViewModel.showDeleteNoticeFragment()
            return@setOnMenuItemClickListener true
        }

        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        editItem?.isVisible = true
        deleteItem?.isVisible = true
        Glide.with(this).load(R.drawable.header9).into(image)
        activity?.collapseToolbar?.title = noticeDetailViewModel.notice.value?.title
        activity?.appBar?.setExpanded(true)

    }

    @SuppressLint("ResourceType")
    private fun showAllertDialogDelete(){
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setTitle("Удалить объявление")
        alertBuilder.setMessage("Вы уверены, что хотите удалить объявление?")
        alertBuilder.setIconAttribute(R.drawable.rdsh_image)
        alertBuilder.setCancelable(true)
        alertBuilder.setPositiveButton(
            "Да"
        ) { _: DialogInterface, _: Int ->
            noticeDetailViewModel.deleteNotice()
            findNavController().navigate(R.id.action_noticeDetailFragment_to_noticeFragment)
        }
        alertBuilder.setNegativeButton(
            "Нет"
        ) { _: DialogInterface, _: Int ->
        }
        alertBuilder.show()
    }

}
