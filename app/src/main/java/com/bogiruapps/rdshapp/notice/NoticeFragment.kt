package com.bogiruapps.rdshapp.notice


import android.os.Bundle
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
        setupListenerOnFub()

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

        noticeViewModel.closeAddNoticeFragmentEvent.observe(this, EventObserver {
            hideAddNotice()
            openNoticeFragment()

        })

        noticeViewModel.notices.observe(this, Observer {
            val notices = it
            setupRecyclerView(notices)
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
        binding.recyclerViewNotice.visibility = View.VISIBLE
        noticeViewModel.initNotices()
    }


    private fun setupRecyclerView(notices: List<Notice>) {
        binding.recyclerViewNotice.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewNotice.adapter = NoticeAdapter(notices)
        hideProgress()
    }

    private fun setupListenerOnFub() {
        binding.fubNotice.setOnClickListener {
            showAddNotice()
        }
    }

    private fun showAddNotice() {
        binding.addNoticeLayout.visibility = View.VISIBLE
        binding.textNotice = "Барболина Мария"
        binding.fubNotice.visibility = View.INVISIBLE
    }

    private fun hideAddNotice() {
        binding.addNoticeLayout.visibility = View.INVISIBLE
        binding.fubNotice.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        pb_notice.visibility = View.INVISIBLE
    }

}
