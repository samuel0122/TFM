package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
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

    fun deleteImage(context: Context, imageUri: Uri) {
        context.contentResolver.delete(imageUri, null, null)
    }

    fun saveImageToStorage(context: Context, bitmap: Bitmap): Uri? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis())
        val fileName = "MyImage_$timeStamp.jpg"
        val folderName = "SAVE_IMAGE_TEST"

        var imageUri: Uri? = null
        var imageFile: File? = null

        var outputStream: OutputStream? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // getting the contentResolver
                context.contentResolver?.also { contentResolver ->
                    // Content resolver will process the contentValues
                    val contentValue = ContentValues().apply {
                        // putting file information in contentValues
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(
                            MediaStore.MediaColumns.RELATIVE_PATH,
                            Environment.DIRECTORY_PICTURES + File.separator + folderName
                        )
                    }

                    // Inserting the contentValues to contentResolver and getting the Uri
                    imageUri = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValue
                    )

                    // Opening an outputStream with the Uri we got
                    outputStream = imageUri?.let { contentResolver.openOutputStream(it) }
                }
            } else {
                val imageDir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .toString() + File.separator + folderName
                )

                if (!imageDir.exists()) imageDir.mkdir()

                imageFile = File(imageDir, fileName)

                outputStream = FileOutputStream(imageFile)
            }

            outputStream?.use { stream ->

                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)) {
                    Log.e("Save Fail", "Failed to save bitmap")
                }
                stream.flush()
            }
        } finally {
            outputStream?.close()
        }

        if (imageFile != null) {
            MediaScannerConnection.scanFile(context, arrayOf(imageFile.toString()), null, null)

            imageUri = Uri.fromFile(imageFile)

            if (imageUri != null)
                imageUri?.toFile()
        }

        return imageUri
    }
}