package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.listeners

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.ICanvasItem

fun interface OnCanvasItemDeleteListener {
    fun onItemDelete(item: ICanvasItem)
}

interface OnCanvasItemDeleteListenerConsumer {
    fun setOnCanvasItemDeleteListener(listener: OnCanvasItemDeleteListener)
}