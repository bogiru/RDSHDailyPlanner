package com.bogiruapps.rdshapp.notice


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentNoticeBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_notice.*
import org.koin.android.viewmodel.ext.android.viewModel

class NoticeFragment : Fragment() {

    private val noticeViewModel: NoticeViewModel by viewModel()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoticeAdapter
    private lateinit var binding: FragmentNoticeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        configureFirebase()
        setupListenerOnFub()

        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice, container, false)
        binding.viewModel = noticeViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner


    }

    private fun setupObserverViewModel() {
        /*showProgress()*/
        noticeViewModel.openChooseSchoolFragmentEvent.observe(this, EventObserver {
            openChooseSchoolFragment()
        })
        noticeViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            hideProgress()
            configureRecyclerView()
        })

        noticeViewModel.openNoticeDetailFragmentEvent.observe(this, EventObserver {
            showANoticeDetail()
            hideProgress()

        })

        noticeViewModel.notices.observe(this, Observer { notices ->
        })
    }

    private fun configureFirebase() {
        val auth = FirebaseAuth.getInstance()
        noticeViewModel.checkUserSchool()
    }

    private fun openChooseSchoolFragment() {
        hideProgress()
        findNavController().navigate(R.id.choseSchoolFragment)
    }

    private fun configureRecyclerView() {
        val linearLayoutManager = GridLayoutManager(activity, 5)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView = binding.recyclerViewNotice

        adapter = NoticeAdapter(noticeViewModel.fetchFirestoreRecyclerOptions(), noticeViewModel)

        binding.recyclerViewNotice.layoutManager = linearLayoutManager
        binding.recyclerViewNotice.adapter = adapter
        adapter.startListening()

    }

    private fun setupListenerOnFub() {
        binding.fubNotice.setOnClickListener {
            showANoticeDetail()
        }
    }

    private fun showANoticeDetail() {
        findNavController().navigate(R.id.noticeDetailFragment)
    }

    private fun hideProgress() {
        pb_notice.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

}
