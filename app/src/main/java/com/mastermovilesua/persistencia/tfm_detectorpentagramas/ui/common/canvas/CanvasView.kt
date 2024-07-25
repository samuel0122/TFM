package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.listeners.OnCanvasItemDeleteListener
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.listeners.OnCanvasItemDeleteListenerConsumer
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.listeners.OnCanvasItemSelectListener
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.listeners.OnCanvasItemSelectListenerConsumer
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.listeners.OnCanvasItemUpdateListener
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.canvas.listeners.OnCanvasItemUpdateListenerConsumer

class CanvasView(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs), GestureDetector.OnGestureListener, OnCanvasItemUpdateListenerConsumer,
    OnCanvasItemSelectListenerConsumer,
    OnCanvasItemDeleteListenerConsumer {
    private var canvasItems = emptyMap<Int, ICanvasItem>()

    private var selectedItemId: Int? = null
    private val selectedItem: ICanvasItem? get() = selectedItemId?.let { canvasItems[selectedItemId] }

    private var canvasInteractionType: CanvasInteractionType = CanvasInteractionType.None

    private val gestureDetector = GestureDetector(context, this)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvasItems.forEach { (_, item) -> item.draw(canvas) }
        selectedItem?.drawSelected(canvas)
    }

    fun updateCanvasItems(newCanvasItems: List<ICanvasItem>) {
        canvasItems = newCanvasItems.associateBy { it.id }
        invalidate()
    }

    fun selectCanvasItem(newSelectedCanvasId: Int?) {
        selectedItemId = newSelectedCanvasId
        invalidate()
    }

    private var onCanvasItemUpdateListener: OnCanvasItemUpdateListener? = null
    override fun setOnCanvasItemUpdateListener(listener: OnCanvasItemUpdateListener) {
        onCanvasItemUpdateListener = listener
    }

    private var onCanvasItemSelectListener: OnCanvasItemSelectListener? = null
    override fun setOnCanvasItemSelectListener(listener: OnCanvasItemSelectListener) {
        onCanvasItemSelectListener = listener
    }

    private var onCanvasItemDeleteListener: OnCanvasItemDeleteListener? = null
    override fun setOnCanvasItemDeleteListener(listener: OnCanvasItemDeleteListener) {
        onCanvasItemDeleteListener = listener
    }

    // Canvas
    override fun onDown(e1: MotionEvent): Boolean {
        canvasInteractionType = selectedItem?.onSelectedDown(e1.x, e1.y)
            ?: CanvasInteractionType.None
        return true
    }

    override fun onShowPress(e1: MotionEvent) {}

    override fun onSingleTapUp(e1: MotionEvent): Boolean {
        when (canvasInteractionType) {
            CanvasInteractionType.Delete -> selectedItem?.let {
                onCanvasItemDeleteListener?.onItemDelete(it)
            }

            else -> {
                onCanvasItemSelectListener?.onItemSelect(
                    canvasItems.values.filter { it.contains(e1.x, e1.y) && it.id != selectedItemId }
                        .randomOrNull()
                )
            }
        }

        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        selectedItem?.let { selectedItem ->
            when (canvasInteractionType) {
                CanvasInteractionType.FullResize -> selectedItem.onResize(distanceX, distanceY)
                CanvasInteractionType.ResizeVertical -> selectedItem.onResize(null, distanceY)
                CanvasInteractionType.ResizeHorizontal -> selectedItem.onResize(distanceX, null)
                CanvasInteractionType.Move -> selectedItem.onDrag(distanceX, distanceY)
                else -> {}
            }
            onCanvasItemUpdateListener?.onItemUpdate(selectedItem)
        }

        return true
    }

    override fun onLongPress(e1: MotionEvent) {}
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean = true
}