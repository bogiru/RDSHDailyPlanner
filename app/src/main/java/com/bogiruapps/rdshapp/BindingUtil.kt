package com.bogiruapps.rdshapp

import androidx.databinding.BindingConversion
import java.text.SimpleDateFormat
import java.util.*

@BindingConversion
fun convertDateToString(date: Date): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.ENGLISH)
    return dateFormat.format(date)
}
