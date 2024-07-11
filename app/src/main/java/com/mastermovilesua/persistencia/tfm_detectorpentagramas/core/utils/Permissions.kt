package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permissions {

    private const val wriExternalPerm = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    private const val readExternalPerm = android.Manifest.permission.READ_EXTERNAL_STORAGE

    private const val useCameraPerm = android.Manifest.permission.CAMERA

    const val PERMISSION_REQUEST_CODE = 123

    fun hasWritePermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, wriExternalPerm) == PackageManager.PERMISSION_GRANTED

    fun requestWritePermission(context: Context, activity: Activity) {
        if (!hasWritePermission(context)) {
            ActivityCompat.requestPermissions(activity, arrayOf(wriExternalPerm), PERMISSION_REQUEST_CODE)
        }
    }

    fun hasCameraPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, useCameraPerm) == PackageManager.PERMISSION_GRANTED


    fun requestCameraePermission(context: Context, activity: Activity) {
        if (!hasCameraPermission(context)) {
            ActivityCompat.requestPermissions(activity, arrayOf(useCameraPerm), PERMISSION_REQUEST_CODE)
        }
    }

}