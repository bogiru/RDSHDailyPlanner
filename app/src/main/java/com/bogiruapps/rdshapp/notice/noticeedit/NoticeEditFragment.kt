package com.bogiruapps.rdshapp.notice.noticeedit


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentNoticeEditBinding
import com.bogiruapps.rdshapp.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

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
        hideBottomNavigation()

        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice_edit, container, false)
        binding.viewModel = noticeEditViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        noticeEditViewModel.openNoticeFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            showNoticeFragment()
        })

        noticeEditViewModel.showSnackbar.observe(viewLifecycleOwner, Observer { message ->
            showSnackbar(view!!, message)
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)
        editItem?.isVisible = false
        deleteItem?.isVisible = false

        if (noticeEditViewModel.checkCreateNoticeStatus()) {
            activity?.main_toolbar?.title = "Создание объявления"
        } else  {
            activity?.main_toolbar?.title = "Редактирование объявления"
        }
    }

    private fun hideBottomNavigation() {
        activity!!.bottomNavigationView.visibility = View.GONE
    }

    private fun showNoticeFragment() {
        findNavController().navigate(R.id.action_noticeEditFragment_to_noticeFragment)
    }
}
