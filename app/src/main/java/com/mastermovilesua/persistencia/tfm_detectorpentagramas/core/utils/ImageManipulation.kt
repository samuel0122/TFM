package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.view.toCanvas

object ImageManipulation {

    fun drawRectanglesOnImage(context: Context, imageUri: Uri, rectangles: List<BoxItem>?): Uri? {
        val bitmap = SaveToMediaStore.loadImageAsBitmap(context, imageUri) ?: return null
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)

        rectangles?.forEach { rect ->
            val canvasRectangle = rect.toCanvas(canvas.width.toFloat(), canvas.height.toFloat())
            canvasRectangle.draw(canvas)
        }

        return SaveToMediaStore.saveImageToTemporalFile(context, mutableBitmap, "SharePage")
    }

}
