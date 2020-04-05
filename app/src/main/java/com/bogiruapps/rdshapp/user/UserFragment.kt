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
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.utils.RC_PICK_FROM_GALLERY
import com.bogiruapps.rdshapp.utils.RC_SIGN_IN


class UserFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModel()
    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureToolbar()
        configureBinding(inflater, container)
        setupObserverViewModel()

        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        binding.user = userViewModel.fetchCurrentUser()
        binding.viewModel = userViewModel
    }

    private fun configureToolbar() {
        val image = activity!!.headerImage
        activity?.collapseToolbar?.title = "Профиль"
        activity?.appBar?.setExpanded(false)

    }

    private fun setupObserverViewModel() {
        userViewModel.showActionPickActivity.observe(viewLifecycleOwner, EventObserver {
            pickImageFromGallery()
        })
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity?.startActivityForResult(intent, RC_PICK_FROM_GALLERY)
    }
}
