package com.bogiruapps.rdshapp.user


import android.annotation.SuppressLint
import android.content.DialogInterface
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
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.utils.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.bind
import androidx.core.os.HandlerCompat.postDelayed
import android.os.Handler
import android.util.Log
import android.widget.Toast


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
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)

        activity?.toolbar?.title = "Профиль"
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun configureBottomNavigation() {
        activity!!.bottomNavigationView.visibility = View.GONE
    }

    private fun setupObserverViewModel() {
        userViewModel.showActionPickActivity.observe(viewLifecycleOwner, EventObserver {
            pickImageFromGallery()
        })

        userViewModel.showAllertDialogEditSchool.observe(viewLifecycleOwner, EventObserver {
            showAllertDialogEditSchool()
        })

        userViewModel.openChooseSchoolFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            hideLoadPb()
            findNavController().navigate(R.id.action_userFragment_to_choseSchoolFragment)
        })

        userViewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                showLoadPb("Загрузка изображения")

            } else {
                hideLoadPb()
            }
        })
    }

    private fun showLoadPb(textLoad: String) {
        binding.loadTextView.text = textLoad

        binding.userLoadLayout.visibility = View.VISIBLE

    }

    private fun hideLoadPb() {
        binding.userLoadLayout.visibility = View.INVISIBLE
    }

    private fun pickImageFromGallery() {
        requestPermissionStorage(this.activity!!)
        if (requestPermissionStorage(this.activity!!)) {
            startActivityForResult(intentPicture(), RC_PICK_FROM_GALLERY)
        }
    }

    @SuppressLint("ResourceType")
    private fun showAllertDialogEditSchool(){
        val alertBuilder = MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
        alertBuilder.setTitle("Сменить школу")
        alertBuilder.setMessage("Вы дейстаительно хотите сменить школу? Это приведет к потере вашего счета")
        alertBuilder.setIconAttribute(R.drawable.rdsh_image)
        alertBuilder.setCancelable(true)
        alertBuilder.setPositiveButton(
            "Да"
        ) { _: DialogInterface, _: Int ->
            showLoadPb("Удаление пользователя из школы")
            userViewModel.deleteUserFromSchool()
        }
        alertBuilder.setNegativeButton(
            "Нет"
        ) { _: DialogInterface, _: Int ->
        }
        alertBuilder.show()
    }

}
