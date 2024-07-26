package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas

import android.graphics.Canvas

interface ICanvasItem {
    var id: Int

    fun draw(canvas: Canvas)
    fun drawSelected(canvas: Canvas)

    fun contains(x: Float, y: Float): Boolean
    fun onSelectedDown(x: Float, y: Float): CanvasInteractionType
    fun onShowPress(x: Float, y: Float)
    fun onClearPress(x: Float, y: Float)

    fun onDrag(xDistance: Float, yDistance: Float)
    fun onResize(xDistance: Float?, yDistance: Float?)
}