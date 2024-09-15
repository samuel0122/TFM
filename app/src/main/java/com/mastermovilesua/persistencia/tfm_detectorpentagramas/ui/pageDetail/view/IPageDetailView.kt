package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pageDetail.view

interface IPageDetailView {
    fun setEditMode(isEditMode: Boolean)

    fun setDisplayCanvas(display: Boolean)

    fun actionAddBox()
    fun actionDeletePage()
    fun actionSharePage()
    fun actionProcessPage()
}