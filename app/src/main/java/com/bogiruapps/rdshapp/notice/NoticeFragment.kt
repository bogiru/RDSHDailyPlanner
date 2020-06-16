package com.bogiruapps.rdshapp.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentNoticeBinding
import com.bogiruapps.rdshapp.utils.hideBottomNavigationView
import com.bogiruapps.rdshapp.utils.hideKeyboard
import com.bogiruapps.rdshapp.utils.showSnackbar
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.activity_main.*
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
        hideBottomNavigationView(activity!!)

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
        noticeViewModel.openChooseSchoolFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            openChooseSchoolFragment()
        })

        noticeViewModel.openNoticeFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            configureRecyclerView()
        })

        noticeViewModel.openNoticeEditFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            showNoticeEdit()
        })

        noticeViewModel.openNoticeDetailFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            showNoticeDetail()
        })

        noticeViewModel.showSnackbar.observe(viewLifecycleOwner, Observer { message ->
            showSnackbar(view!!, message)
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun openChooseSchoolFragment() {
        findNavController().navigate(R.id.action_noticeFragment_to_choseSchoolFragment)
    }

    private fun configureRecyclerView() {
        val options = getFirestoreRecyclerOptions()
        adapter = NoticeAdapter(options, noticeViewModel)
        binding.noticeRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.noticeRecyclerView.adapter = adapter
    }

    private fun getFirestoreRecyclerOptions(): FirestoreRecyclerOptions<Notice> {
        val queryNotices = noticeViewModel.queryNotices.value
        return FirestoreRecyclerOptions.Builder<Notice>()
            .setQuery(queryNotices!!, Notice::class.java)
            .setLifecycleOwner(this)
            .build()
    }

    private fun showNoticeEdit() {
        findNavController().navigate(R.id.action_noticeFragment_to_noticeEditFragment)
    }

    private fun showNoticeDetail() {
        findNavController().navigate(R.id.action_noticeFragment_to_noticeDetailFragment)
    }
}
