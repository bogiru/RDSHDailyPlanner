package com.bogiruapps.rdshapp.notice


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.UserRemoteDataSource
import com.bogiruapps.rdshapp.UserRepositoryImpl
import com.bogiruapps.rdshapp.databinding.FragmentNoticeBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_notice.*

class NoticeFragment : Fragment() {

    private lateinit var noticeViewModel: NoticeViewModel

    private lateinit var binding: FragmentNoticeBinding

    private lateinit var db: FirebaseFirestore
    private lateinit var userRepository: UserRepositoryImpl
    private lateinit var userDataSource: UserRemoteDataSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureViewModel()
        configureBinding(inflater, container)
        setupObserverViewModel()

        return binding.root
    }

    private fun configureViewModel() {
        db = FirebaseFirestore.getInstance()
        userDataSource = UserRemoteDataSource.getInstance(db)
        userRepository = UserRepositoryImpl.getInstance(userDataSource)
        val factory = NoticeViewModelFactory(userRepository)
        noticeViewModel = ViewModelProviders.of(this, factory).get(NoticeViewModel::class.java)
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notice, container, false)
        binding.viewModel = noticeViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }
    private fun setupObserverViewModel() {
        noticeViewModel.notices.observe(this, Observer {
            val notices = it
            setupRecyclerView(notices)
        })
    }

    private fun setupRecyclerView(notices: List<String>) {
        binding.recyclerViewNotice.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewNotice.adapter = NoticeAdapter(notices)
        hideProgress()
    }

private fun hideProgress() {
    pb_notice.visibility = View.INVISIBLE
    }


}
