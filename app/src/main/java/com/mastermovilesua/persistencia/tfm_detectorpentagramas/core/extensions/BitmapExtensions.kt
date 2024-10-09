package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}