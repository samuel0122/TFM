package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas

import android.content.Context
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.view.BoxCanvasItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CanvasItemFactory @Inject constructor(
    private val context: Context
) {
    fun createCanvasItem(boxItem: BoxItem, canvasWidth: Float, canvasHeight: Float): BoxCanvasItem {
        return BoxCanvasItem(
            id = boxItem.id,
            x = boxItem.x * canvasWidth,
            y = boxItem.y * canvasHeight,
            width = boxItem.width * canvasWidth,
            height = boxItem.height * canvasHeight,
            context = context
        )
    }
}
