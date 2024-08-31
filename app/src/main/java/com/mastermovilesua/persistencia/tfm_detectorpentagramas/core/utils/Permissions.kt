package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils

import android.app.Activity
import android.os.Build
import androidx.fragment.app.Fragment
import com.vmadalin.easypermissions.EasyPermissions

object Permissions {

    const val REQUEST_CODE_CAMERA = 100
    const val REQUEST_CODE_NOTIFICATIONS = 101

    fun requestCameraPermissions(activity: Activity) {
        EasyPermissions.requestPermissions(
            host = activity,
            rationale = "Oiaio",
            requestCode = REQUEST_CODE_CAMERA,
            perms = arrayOf(android.Manifest.permission.CAMERA)
        )
    }

    fun requestCameraPermissions(fragment: Fragment) {
        EasyPermissions.requestPermissions(
            host = fragment,
            rationale = "Oiaio",
            requestCode = REQUEST_CODE_CAMERA,
            perms = arrayOf(android.Manifest.permission.CAMERA)
        )
    }

    fun requestNotificationsPermissions(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            EasyPermissions.requestPermissions(
                host = activity,
                rationale = "Notifications and background dats sync permission is recommended to notify of background activity.",
                requestCode = REQUEST_CODE_NOTIFICATIONS,
                perms = arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS,
                    android.Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                host = activity,
                rationale = "Notifications permission is recommended to notify of background activity.",
                requestCode = REQUEST_CODE_NOTIFICATIONS,
                perms = arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }
    }

    fun requestNotificationsPermissions(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                host = fragment,
                rationale = "Notifications permission is recommended to notify of background activity.",
                requestCode = REQUEST_CODE_NOTIFICATIONS,
                perms = arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
            )
        }
    }

}