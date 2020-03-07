package com.bogiruapps.rdshapp.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentNoticeBinding
import com.bogiruapps.rdshapp.utils.GRID_SPAN_COUNT
import com.bogiruapps.rdshapp.utils.hideKeyboard
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notice.*
import kotlinx.android.synthetic.main.notice_item.*
import org.koin.android.viewmodel.ext.android.viewModel

class NoticeFragment : Fragment() {

    private val noticeViewModel: NoticeViewModel by viewModel()

    private lateinit var adapter: NoticeAdapter
    private lateinit var binding: FragmentNoticeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        noticeViewModel.checkUserSchool()
        configureToolbar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.hideKeyboard()
    }



    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice, container, false)
        binding.viewModel = noticeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        noticeViewModel.openChooseSchoolFragmentEvent.observe(this, EventObserver {
            openChooseSchoolFragment()
        })

        noticeViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            configureRecyclerView()
        })

        noticeViewModel.openNoticeEditFragmentEvent.observe(this, EventObserver {
            findNavController().navigate(R.id.action_noticeFragment_to_noticeEditFragment)

        })

        noticeViewModel.openNoticeDetailFragmentEvent.observe(this, EventObserver {
            showNoticeDetail()
        })

        noticeViewModel.dataLoading.observe(this, Observer{ isShowProgress ->
            if (isShowProgress) {
                binding.pbNotice.visibility = View.VISIBLE
            } else {
                binding.pbNotice.visibility = View.INVISIBLE
            }
        })

    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)

        activity?.window?.decorView?.systemUiVisibility = View.VISIBLE
        activity?.collapseToolbar?.title = "Объявления"
        activity?.appBar?.setExpanded(false)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun openChooseSchoolFragment() {
        hideProgress()
        findNavController().navigate(R.id.choseSchoolFragment)
    }

    private fun configureRecyclerView() {
        //val layoutManager = GridLayoutManager(activity, GRID_SPAN_COUNT, GridLayoutManager.HORIZONTAL, false)
        val options = getFirestoreRecyclerOptions()
        adapter = NoticeAdapter(options, noticeViewModel)
        binding.recyclerViewNotice.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewNotice.adapter = adapter
    }

    private fun getFirestoreRecyclerOptions(): FirestoreRecyclerOptions<Notice> {
        val query = noticeViewModel.query.value
        return FirestoreRecyclerOptions.Builder<Notice>()
            .setQuery(query!!, Notice::class.java)
            .setLifecycleOwner(this)
            .build()
    }

    private fun showNoticeDetail() {
        val extras = FragmentNavigatorExtras(
            imageViewNotice to "imageViewNotice"
        )
        findNavController().navigate(R.id.action_noticeFragment_to_noticeDetailFragment, null, null, extras)
    }

    private fun hideProgress() {
        pb_notice.visibility = View.INVISIBLE
    }
}
