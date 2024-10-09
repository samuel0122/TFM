package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

object SaveToMediaStore {
    /**
     * Load an Image Uri as a Byte Array
     */
    fun loadImageAsByteArray(context: Context, imageUri: Uri): ByteArray? {
        return try {
            val loadFileStream = context.contentResolver.openInputStream(imageUri)
            loadFileStream?.use { input ->
                input.readBytes()
            }
        } catch (e: FileNotFoundException) {
            Log.e("loadImageAsByteArray", "File Not Found Exception: ${e.message}")
            null
        } catch (e: IOException) {
            Log.e("loadImageAsByteArray", "IO Exception: ${e.message}")
            null
        }
    }

    fun loadImageAsBitmap(context: Context, imageUri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT < 28)
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            else {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                inputStream?.use {
                    BitmapFactory.decodeStream(it)
                }
            }
        } catch (e: FileNotFoundException) {
            Log.e("loadImageAsByteArray", "File Not Found Exception: ${e.message}")
            null
        } catch (e: IOException) {
            Log.e("loadImageAsByteArray", "IO Exception: ${e.message}")
            null
        }
    }

    /**
     * Save Byte Array Image to File
     */
    private fun saveImageFromByteArray(
        context: Context,
        imageBytes: ByteArray,
        saveUri: Uri
    ): Boolean {
        return try {
            val saveFileStream = context.contentResolver.openOutputStream(saveUri)
            saveFileStream?.use { output ->
                output.write(imageBytes)
            }
            true
        } catch (e: FileNotFoundException) {
            Log.e("saveImageFromByteArray", "File Not Found Exception: ${e.message}")
            false
        } catch (e: IOException) {
            Log.e("saveImageFromByteArray", "IO Exception: ${e.message}")
            false
        }
    }

    fun saveImageToInternalStorage(
        context: Context,
        imageBytes: ByteArray,
        fileName: String?
    ): Uri? {
        val saveFile = getFileForImageFile(context, getImageFileName(fileName ?: "MyImage"))
        val saveFileUri = getImageUriForImageFile(context, saveFile)

        return if (saveImageFromByteArray(context, imageBytes, saveFileUri)) saveFileUri
        else null
    }


    fun saveImageToTemporalFile(context: Context, imageBytes: ByteArray): File? {
        val saveFile = getTemporalFileForImageFile(context, "TempFile")
        val saveFileUri = getImageUriForImageFile(context, saveFile)

        return if (saveImageFromByteArray(context, imageBytes, saveFileUri)) saveFile
        else null
    }

    /**
     * Save Bitmap Image to File
     */
    private fun saveImageFromBitmap(
        context: Context,
        imageBitmap: Bitmap,
        saveUri: Uri,
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): Boolean {
        return try {
            val saveFileStream = context.contentResolver.openOutputStream(saveUri)
            saveFileStream?.use { output ->
                imageBitmap.compress(compressFormat, 100, output)
            }
            true
        } catch (e: FileNotFoundException) {
            Log.e("saveImageToInternalStorage", "File Not Found Exception: ${e.message}")
            false
        } catch (e: IOException) {
            Log.e("saveImageToInternalStorage", "IO Exception: ${e.message}")
            false
        }
    }

    fun saveImageToInternalStorage(
        context: Context,
        imageBitmap: Bitmap,
        fileName: String?,
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): Uri? {
        val saveFile = getFileForImageFile(context, getImageFileName(fileName ?: "MyImage"))
        val saveFileUri = getImageUriForImageFile(context, saveFile)

        return if (saveImageFromBitmap(
                context,
                imageBitmap,
                saveFileUri,
                compressFormat
            )
        ) saveFileUri
        else null
    }

    fun saveImageToTemporalFile(
        context: Context, imageBitmap: Bitmap,
        fileName: String?,
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): Uri? {
        val saveFile = getTemporalFileForImageFile(context, fileName ?: "TempImage")
        val saveFileUri = getImageUriForImageFile(context, saveFile)

        return if (saveImageFromBitmap(
                context,
                imageBitmap,
                saveFileUri,
                compressFormat
            )
        ) saveFileUri
        else null
    }

    /**
     * Delete file at Uri
     */
    fun deleteImage(context: Context, imageUri: Uri) {
        context.contentResolver.delete(imageUri, null, null)
    }

    /**
     * Get properties
     */
    private fun getImageFileName(imageName: String = "MyImage"): String {
        val timeStamp: String = SimpleDateFormat(
            "yyyy-MM-dd_HH-mm-ss-SSS",
            Locale.US
        ).format(System.currentTimeMillis())
        return "${imageName}_${timeStamp}.jpeg"
    }

    private fun getFileForImageFile(context: Context, imageFileName: String): File {
        return File(context.filesDir, imageFileName)
    }

    private fun getTemporalFileForImageFile(context: Context, imageFileName: String): File {
        return File(context.cacheDir, imageFileName)
    }

    private fun getImageUriForImageFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, MusicScoreBooksContract.AUTHORITY, file)
    }
}