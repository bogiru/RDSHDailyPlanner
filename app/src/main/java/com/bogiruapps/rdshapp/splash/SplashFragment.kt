package com.bogiruapps.rdshapp.splash


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.UserRemoteDataSource
import com.bogiruapps.rdshapp.UserRepositoryImpl
import com.bogiruapps.rdshapp.databinding.FragmentSplashBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {

    private lateinit var splashViewModel: SplashViewModel

    private lateinit var binding: FragmentSplashBinding

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
        val factory = SplashViewModelFactory(userRepository)
        splashViewModel = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        binding.viewModel = splashViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }
    private fun setupObserverViewModel() {
        /*showProgress()*/
        splashViewModel.openChooseSchoolFragmentEvent.observe(this, EventObserver {
            openChooseSchoolFragment()
        })
        splashViewModel.openNoticeFragmentEvent.observe(this, EventObserver {
            openNoticeFragment()
        })
    }

    private fun configureFirebase() {
        val auth = FirebaseAuth.getInstance()
        splashViewModel.checkUserSchool(auth.currentUser)
    }

    private fun openChooseSchoolFragment() {
        hideProgress()
        findNavController().navigate(R.id.choseSchoolFragment)
    }

    private fun openNoticeFragment() {
        hideProgress()
        findNavController().navigate(R.id.noticeFragment)
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
