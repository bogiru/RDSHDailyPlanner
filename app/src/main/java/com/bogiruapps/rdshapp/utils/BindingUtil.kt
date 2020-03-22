package com.bogiruapps.rdshapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.bumptech.glide.Glide
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
