package com.bogiruapps.rdshapp.user


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.bogiruapps.rdshapp.databinding.FragmentUserBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import android.content.Intent
import androidx.lifecycle.Observer
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.utils.*
import org.koin.android.ext.android.bind


class UserFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModel()
    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureToolbar()
        configureBottomNavigation()
        configureBinding(inflater, container)
        setupObserverViewModel()

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_PICK_FROM_GALLERY -> userViewModel.fetchPictureByUser(resultCode, data?.data)
        }
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        binding.user = userViewModel.fetchCurrentUser()
        binding.viewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun configureToolbar() {
        activity?.toolbar?.title = "Профиль"

    }

    private fun configureBottomNavigation() {
        activity!!.bottomNavigationView.visibility = View.GONE
    }

    private fun setupObserverViewModel() {
        userViewModel.showActionPickActivity.observe(viewLifecycleOwner, EventObserver {
            pickImageFromGallery()
        })

        userViewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.loadLayout.visibility = View.VISIBLE
                binding.userPb.visibility = View.VISIBLE
            } else {
                binding.loadLayout.visibility = View.INVISIBLE
                binding.userPb.visibility = View.INVISIBLE
            }
        })
    }

    private fun pickImageFromGallery() {
        requestPermissionStorage(this.activity!!)
        if (requestPermissionStorage(this.activity!!)) {
            startActivityForResult(intentPicture(), RC_PICK_FROM_GALLERY)
        }
    }
}
