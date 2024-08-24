package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

data class PageWithBoxesItem(
    val page: PageItem,
    val boxes: List<BoxItem>
)
