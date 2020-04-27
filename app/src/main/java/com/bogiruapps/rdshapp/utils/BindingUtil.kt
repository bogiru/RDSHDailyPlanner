package com.bogiruapps.rdshapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.bogiruapps.rdshapp.R
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

@BindingConversion
fun convertDateToString(date: Date): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.ENGLISH)
    return dateFormat.format(date)
}

@BindingAdapter("setDateNoticeItem")
fun setDateNoticeItem(view: TextView, date: Date) {
    val dateFormat = SimpleDateFormat("dd MMM", Locale("Ru"))

    view.text = dateFormat.format(date).toString()
}

@BindingAdapter("imageDrawable")
fun bindImage(view: ImageView, img: Int) {
    Glide.with(view.context).load(img).centerCrop().into(view)
}

@BindingAdapter("imageDrawable")
fun bindImage(view: ImageView, avatar: String?) {
    avatar?.let {

            val storageReference = FirebaseStorage.getInstance().reference.child(avatar)
            storageReference.downloadUrl.addOnCompleteListener {
                try {
                    Glide.with(view.context).load(it.result).error(R.drawable.noavatar).into(view)
                }catch (e: Exception) {
                    Glide.with(view.context).load(R.drawable.noavatar).into(view)
                }
             }

    }
}

@BindingAdapter("loadImageEvent")
fun loadImageEvent(view: ImageView, indexImage: Int) {
    val storageReference =
        FirebaseStorage.getInstance().reference.child("backgroundEvents/$indexImage.png")
    storageReference.downloadUrl.addOnCompleteListener {
        try {
            Glide.with(view.context).load(it.result).error(R.drawable.noavatar).into(view)
        } catch (e: RuntimeExecutionException) {
            Glide.with(view.context).load(R.drawable.noavatar).into(view)
        }
    }
}
