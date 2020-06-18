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

@BindingAdapter("setAuthorChatsItem")
fun setAuthorChatsItem(view: TextView, authorName: String) {
    view.text = authorName.split(" ")[0]
}

@BindingAdapter("setTitleLetterToImage")
fun setTitleLetterToImage(view: TextView, titleChat: String) {
    view.text = titleChat[0].toString().toUpperCase()
}

@BindingAdapter("imageDrawable")
fun bindImage(view: ImageView, img: Int) {
    Glide.with(view.context).load(img).centerCrop().into(view)
}

@BindingAdapter("loadIdInCircle")
fun loadIdInCircle(view: ImageView, userId: String?) {
    val url = FirebaseStorage
        .getInstance()
        .reference.child("images/userPicture/$userId")
        .downloadUrl

    url.addOnSuccessListener { result ->
        val glide = Glide.with(view.context)
        glide
            .load(result)
            .error(glide.load(R.drawable.noavatar))
            .into(view)
    }

    url.addOnCanceledListener {
        Glide.with(view.context).load(R.drawable.noavatar).into(view)
    }

    url.addOnFailureListener {
        Glide.with(view.context).load(R.drawable.noavatar).into(view)
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
            Glide.with(view.context).load(R.drawable.error_load_image_school_event).into(view)
        }
    }
}
