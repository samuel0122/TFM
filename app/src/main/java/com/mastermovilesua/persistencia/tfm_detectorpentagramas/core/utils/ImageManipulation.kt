package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.view.toCanvas
import java.io.IOException

object ImageManipulation {

    fun drawRectanglesOnImage(context: Context, imageUri: Uri, rectangles: List<BoxItem>?): Uri {
        val bitmap = if (Build.VERSION.SDK_INT < 28)
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        else {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, imageUri))
        }

        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)

        rectangles?.forEach { rect ->
            val canvasRectangle = rect.toCanvas(canvas.width.toFloat(), canvas.height.toFloat())
            canvasRectangle.draw(canvas)
        }

        val tempFile = SaveToMediaStore.getTemporalFileForImageFile(
            context,
            SaveToMediaStore.getImageFileName("SharePage")
        )
        val tempUri = SaveToMediaStore.getImageUriForImageFile(context, tempFile)

        try {
            context.contentResolver.openOutputStream(tempUri)?.use { outputStream ->
                mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            }

        } catch (e: IOException) {
            Log.e("drawRectanglesOnImage", e.message.orEmpty())
        }

        return tempUri
    }

}
