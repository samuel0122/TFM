package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.interfaces

fun interface OnItemClickListener<T> {
    fun onItemClick(item: T)
}

interface OnItemClickListenerConsumer<T> {
    fun setOnItemClickListener(listener: OnItemClickListener<T>)
}