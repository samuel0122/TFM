package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.CanvasInteractionType
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.ICanvasItem

data class Circle(
    var cx: Float,
    var cy: Float,
    val radius: Float
)

fun Circle.contains(x: Float, y: Float): Boolean {
    val dx = x - this.cx
    val dy = y - this.cy
    return dx * dx + dy * dy <= this.radius * this.radius
}

class BoxCanvasItem (
    override var id: Int,
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
    private val context: Context?
) : ICanvasItem {

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.RED
        strokeWidth = 4f
    }
    private val paintResizeButtons = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }
    private val paintDeleteButton = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    private val circleDelete: Circle get() = Circle(x, y, 30f)
    private val circleFullResize: Circle get() = Circle(x + width, y + height, 30f)
    private val circleVerticalResize: Circle get() = Circle(x + (width / 2f), y + height, 30f)
    private val circleHorizontalResize: Circle get() = Circle(x + width, y + (height / 2f), 30f)

    private val deleteIcon: Drawable? = context?.let { ContextCompat.getDrawable(context, R.drawable.ic_close) }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(x, y, x + width, y + height, paint)
    }

    override fun drawSelected(canvas: Canvas) {
        arrayOf(circleFullResize, circleVerticalResize, circleHorizontalResize).forEach {
            canvas.drawCircle(it.cx, it.cy, it.radius, paintResizeButtons)
        }

        deleteIcon?.run {
            canvas.drawCircle(
                circleDelete.cx,
                circleDelete.cy,
                circleDelete.radius,
                paintDeleteButton
            )
            setBounds(
                (circleDelete.cx - circleDelete.radius).toInt(),
                (circleDelete.cy - circleDelete.radius).toInt(),
                (circleDelete.cx + circleDelete.radius).toInt(),
                (circleDelete.cy + circleDelete.radius).toInt()
            )
            draw(canvas)
        }
    }

    override fun contains(x: Float, y: Float): Boolean {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height
    }

    override fun onSelectedDown(x: Float, y: Float): CanvasInteractionType {
        return when {
            circleFullResize.contains(x, y) -> CanvasInteractionType.FullResize
            circleVerticalResize.contains(x, y) -> CanvasInteractionType.ResizeVertical
            circleHorizontalResize.contains(x, y) -> CanvasInteractionType.ResizeHorizontal
            circleDelete.contains(x, y) -> CanvasInteractionType.Delete
            else -> CanvasInteractionType.Move
        }
    }

    override fun onShowPress(x: Float, y: Float) {
        paint.color = Color.YELLOW
    }

    override fun onClearPress(x: Float, y: Float) {
        paint.color = Color.RED
    }

    override fun onDrag(xDistance: Float, yDistance: Float) {
        x -= xDistance
        y -= yDistance
    }

    override fun onResize(xDistance: Float?, yDistance: Float?) {
        xDistance?.let { width -= xDistance }
        yDistance?.let { height -= yDistance }
    }
}

fun BoxItem.toCanvas(canvasWidth: Float, canvasHeight: Float, context: Context? = null): BoxCanvasItem =
    BoxCanvasItem(id, x * canvasWidth, y * canvasHeight, width * canvasWidth, height * canvasHeight, context)

fun BoxCanvasItem.toDomain(canvasWidth: Float, canvasHeight: Float): BoxItem =
    BoxItem(id, x / canvasWidth, y / canvasHeight, width / canvasWidth, height / canvasHeight)