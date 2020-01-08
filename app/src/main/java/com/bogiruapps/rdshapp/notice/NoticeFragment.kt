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
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.UserRemoteDataSource
import com.bogiruapps.rdshapp.UserRepositoryImpl
import com.bogiruapps.rdshapp.databinding.FragmentNoticeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_notice.*
import kotlinx.android.synthetic.main.fragment_notice.view.*
import kotlinx.android.synthetic.main.notice_empty.view.*
import kotlinx.android.synthetic.main.notice_full.view.*

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
        configureFirebase()

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
        /*showProgress()*/
        noticeViewModel.openChooseSchoolFragmentEvent.observe(this, EventObserver {
            openChooseSchoolFragment()
        })
        noticeViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            openNoticeFragment()
        })
    }

    private fun configureFirebase() {
        val auth = FirebaseAuth.getInstance()
        noticeViewModel.checkUserSchool(auth.currentUser)
    }

    private fun openChooseSchoolFragment() {
        hideProgress()
        findNavController().navigate(R.id.choseSchoolFragment)
    }

    private fun openNoticeFragment() {
        hideProgress()
        showNotice()
    }

    private fun showNotice() {
        include_notice_full.visibility = View.VISIBLE
    }

    private fun showProgress() {
        pb_notice.visibility = View.VISIBLE
    }


private fun hideProgress() {
    pb_notice.visibility = View.INVISIBLE
    }


}
