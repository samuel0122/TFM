package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permissions {

    private const val wriExternalPerm = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    private const val readExternalPerm = android.Manifest.permission.READ_EXTERNAL_STORAGE

    const val PERMISSION_REQUEST_CODE = 123

    fun hasPermission(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context, wriExternalPerm) == PackageManager.PERMISSION_GRANTED)
    }

    fun requestPermission(context: Context, activity: Activity) {
        if (!hasPermission(context)) {
            ActivityCompat.requestPermissions(activity, arrayOf(wriExternalPerm), PERMISSION_REQUEST_CODE)
        }
    }
}