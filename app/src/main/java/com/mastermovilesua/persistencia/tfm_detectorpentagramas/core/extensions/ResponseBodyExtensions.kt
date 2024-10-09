package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.ResponseBody
import java.io.ByteArrayOutputStream

fun ResponseBody.toByteArray(): ByteArray {
    return byteStream().use { inputStream ->
        val outputStream = ByteArrayOutputStream()
        inputStream.copyTo(outputStream)
        outputStream.toByteArray()
    }
}

fun ResponseBody.toBitmap(): Bitmap? {
    return BitmapFactory.decodeStream(byteStream())
}