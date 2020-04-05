package com.bogiruapps.rdshapp.utils

import android.os.storage.StorageManager
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.bogiruapps.rdshapp.R
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

@BindingConversion
fun convertDateToString(date: Date): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.ENGLISH)
    return dateFormat.format(date)
}

@BindingAdapter("imageDrawable")
fun bindImage(view: ImageView, img: Int) {
    Glide.with(view.context).load(img).centerCrop().into(view)
}

@BindingAdapter("imageDrawable")
fun bindImage(view: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val storageReference = FirebaseStorage.getInstance().reference
        storageReference.activeUploadTasks
        var imagesRef: StorageReference? = storageReference.child("avatars")
        var spaceRef = imagesRef?.child("p4.jpg")
        var f = spaceRef!!.downloadUrl.addOnCompleteListener {
            Glide.with(view.context).load(it.result).into(view)
        }

    }

}
