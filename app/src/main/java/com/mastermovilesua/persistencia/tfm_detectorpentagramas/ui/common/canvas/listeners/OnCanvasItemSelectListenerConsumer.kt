package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.listeners

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.ICanvasItem

fun interface OnCanvasItemSelectListener {
    fun onItemSelect(item: ICanvasItem?)
}

interface OnCanvasItemSelectListenerConsumer  {
    fun setOnCanvasItemSelectListener(listener: OnCanvasItemSelectListener)
}