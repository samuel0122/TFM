package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import okhttp3.ResponseBody
import java.io.ByteArrayOutputStream

fun ResponseBody.saveFile(context: Context, fileName: String): Uri {
    val saveFile = SaveToMediaStore.getFileForImageFile(context, fileName)

    byteStream().use { inputStream->
        saveFile.outputStream().use { outputStream->
            inputStream.copyTo(outputStream)
        }
    }

    return SaveToMediaStore.getImageUriForImageFile(context, saveFile)
}

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