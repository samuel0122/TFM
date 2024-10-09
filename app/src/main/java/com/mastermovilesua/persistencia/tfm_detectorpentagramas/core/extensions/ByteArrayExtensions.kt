package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}