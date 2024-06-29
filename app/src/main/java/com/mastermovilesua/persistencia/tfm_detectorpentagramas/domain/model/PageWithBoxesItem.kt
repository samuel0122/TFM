package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.PageWithBoxes

data class PageWithBoxesItem(
    val page: PageItem,
    val boxes: List<BoxItem>
)

fun PageWithBoxes.toDomain() = PageWithBoxesItem(page.toDomain(), boxes.map { it.toDomain() })