package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.Locale

object SaveToMediaStore {

    fun getImageFileName(imageName: String = "MyImage", index: Int = 1): String {
        val timeStamp: String = SimpleDateFormat(
            "yyyy-MM-dd_HH-mm-ss-SSS",
            Locale.US
        ).format(System.currentTimeMillis())
        return "${imageName}_${timeStamp}_${index}.jpeg"
    }

    fun getFileForImageFile(context: Context, imageFileName: String): File {
        return File(context.filesDir, imageFileName)
    }

    fun getTemporalFileForImageFile(context: Context, imageFileName: String): File {
        return File(context.cacheDir, imageFileName)
    }

    fun getImageUriForImageFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, MusicScoreBooksContract.AUTHORITY, file)
    }

    fun saveImageToInternalStorage(
        context: Context,
        imageUri: Uri,
        imageName: String = "MyImage",
        index: Int = 1
    ): Uri {
        val copyImageFile = getFileForImageFile(context, getImageFileName(imageName, index))
        val copyImageUri = getImageUriForImageFile(context, copyImageFile)

        try {
            val selectedImageStream = context.contentResolver.openInputStream(imageUri)
            val copyImageStream = context.contentResolver.openOutputStream(copyImageUri)

            selectedImageStream?.use { input ->
                copyImageStream?.use { output ->
                    input.copyTo(output)
                }
            }

        } catch (e: FileNotFoundException) {
            Log.e("saveImageToInternalStorage", e.message.orEmpty())
        }

        return copyImageUri
    }

    fun loadTemporalFile(context: Context, uri: Uri): File {
        val copyImageFile = getTemporalFileForImageFile(context, "copy")
        val copyImageUri = getImageUriForImageFile(context, copyImageFile)

        try {
            val selectedImageStream = context.contentResolver.openInputStream(uri)
            val copyImageStream = context.contentResolver.openOutputStream(copyImageUri)

            selectedImageStream?.use { input ->
                copyImageStream?.use { output ->
                    input.copyTo(output)
                }
            }

        } catch (e: FileNotFoundException) {
            Log.e("saveImageToInternalStorage", e.message.orEmpty())
        }

        return copyImageFile
    }

    fun deleteImage(context: Context, imageUri: Uri) {
        context.contentResolver.delete(imageUri, null, null)
    }
}