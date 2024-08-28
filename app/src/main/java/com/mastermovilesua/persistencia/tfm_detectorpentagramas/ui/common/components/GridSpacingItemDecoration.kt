package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.components

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val numberColumns: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % numberColumns

        if (includeEdge) {
            outRect.left = spacing - column * spacing / numberColumns
            outRect.right = (column + 1) * spacing / numberColumns

            if (position < numberColumns) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / numberColumns
            outRect.right = spacing - (column + 1) * spacing / numberColumns
            if (position >= numberColumns) {
                outRect.top = spacing // item top
            }
        }
    }
}