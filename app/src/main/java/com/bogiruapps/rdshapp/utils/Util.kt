package com.bogiruapps.rdshapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.hasPermissions

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun showSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}

fun hideBottomNavigationView(activity: Activity) {
    activity.bottomNavigationView.visibility = View.GONE
}

@AfterPermissionGranted(RC_IMAGE_PERMS)
fun requestPermissionStorage(activity: Activity): Boolean {
    if (!hasPermissions(activity, PERMS_EXT_STORAGE)) {
        EasyPermissions.requestPermissions(
            activity, "Приложению требуется доступ к галерее", RC_IMAGE_PERMS, PERMS_EXT_STORAGE)
        return(hasPermissions(activity, PERMS_EXT_STORAGE))
    }
    return true
}

fun intentPicture(): Intent {
    return Intent().apply {
        action = Intent.ACTION_PICK
        type = "image/*"
    }
}
