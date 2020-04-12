package com.bogiruapps.rdshapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.bogiruapps.rdshapp.R
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import java.lang.Exception
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
fun bindImage(view: ImageView, avatar: String?) {
    avatar?.let {
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(avatar)
        storageReference.downloadUrl.addOnCompleteListener {
            try {
                Glide.with(view.context).load(it.result).error(R.drawable.noavatar).into(view)
            }catch (e: RuntimeExecutionException) {
                Glide.with(view.context).load(R.drawable.noavatar).into(view)
            }
        }
    }
}
