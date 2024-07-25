package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.listeners

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.ICanvasItem

fun interface OnCanvasItemUpdateListener {
    fun onItemUpdate(item: ICanvasItem)
}

interface OnCanvasItemUpdateListenerConsumer  {
    fun setOnCanvasItemUpdateListener(listener: OnCanvasItemUpdateListener)
}