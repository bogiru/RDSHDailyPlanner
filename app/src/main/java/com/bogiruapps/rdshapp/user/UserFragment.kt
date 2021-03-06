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
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.android.get


class UserFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModel()
    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureToolbar()
        hideBottomNavigationView(activity!!)
        configureBinding(inflater, container)
        setupObserverViewModel()

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_PICK_FROM_GALLERY -> {
                binding.loadTextView.text = "Загрузка изображения"
                userViewModel.fetchPictureFromGallery(resultCode, data?.data)
            }
        }
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        binding.user = userViewModel.user
        binding.viewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)
        editItem?.isVisible = false
        deleteItem?.isVisible = false

        activity?.main_toolbar?.title = "Профиль"
    }

    private fun setupObserverViewModel() {
        userViewModel.showActionPickActivity.observe(viewLifecycleOwner, EventObserver {
            pickImageFromGallery()
        })

        userViewModel.showAlertDialogEditSchool.observe(viewLifecycleOwner, EventObserver {
            showAlertDialogEditSchool()
        })

        userViewModel.openChooseSchoolFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_userFragment_to_choseSchoolFragment)
        })

        userViewModel.imageLoadingToRemoteStorageCompleteEvent.observe(viewLifecycleOwner, EventObserver {
            showSnackbar(view!!, "Изменение изображения профиля может занять несколько минут")
            setImageToView()
        })

        userViewModel.showSnackbar.observe(viewLifecycleOwner, Observer { message ->
            showSnackbar(view!!, message)
        })
    }

    private fun setImageToView() {
        val profileImageView = activity!!.findViewById<ImageView>(R.id.drawer_header_profile_image)
        val storageReference =
            FirebaseStorage.getInstance().reference.child("images/userPicture/${userViewModel.user.id}")

        storageReference.downloadUrl.addOnSuccessListener { result ->
            val glideHeader = Glide.with(profileImageView)
            val glideUser = Glide.with(binding.profileImage)

            glideHeader
                .load(result)
                .error(glideHeader.load(R.drawable.noavatar))
                .into(profileImageView)

            glideUser
                .load(result)
                .error(glideHeader.load(R.drawable.noavatar))
                .into((binding.profileImage))
        }

    }

    private fun pickImageFromGallery() {
        requestPermissionStorage(this.activity!!)
        if (requestPermissionStorage(this.activity!!)) {
            startActivityForResult(intentPicture(), RC_PICK_FROM_GALLERY)
        }
    }

    @SuppressLint("ResourceType")
    private fun showAlertDialogEditSchool(){
        val alertBuilder = MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
        alertBuilder.apply {
            setTitle("Сменить школу")
            setMessage("Вы действительно хотите сменить школу?")
            setIconAttribute(R.drawable.rdsh_image)
            setCancelable(true)
            setPositiveButton(
                "Да"
            ) { _: DialogInterface, _: Int ->
                binding.loadTextView.text = "Удаление пользователя из школы"
                userViewModel.deleteUserFromSchool()
            }
            setNegativeButton(
                "Нет"
            ) { _: DialogInterface, _: Int ->
            }
            show()
        }
    }

}
