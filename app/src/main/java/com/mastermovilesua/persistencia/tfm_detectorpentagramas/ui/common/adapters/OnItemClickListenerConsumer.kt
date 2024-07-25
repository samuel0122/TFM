package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.adapters

import android.view.View

fun interface OnItemClickListener<T> {
    fun onItemClick(item: T, view: View)
}

interface OnItemClickListenerConsumer<T> {
    fun setOnItemClickListener(listener: OnItemClickListener<T>)
}