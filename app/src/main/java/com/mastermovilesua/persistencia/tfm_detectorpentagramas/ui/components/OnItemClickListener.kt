package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components

interface OnItemClickListener <T> {
    fun setOnItemClickListener(listener: (T) -> Unit)
}