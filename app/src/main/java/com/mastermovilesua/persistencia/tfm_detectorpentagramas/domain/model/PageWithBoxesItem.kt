package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.PageWithBoxesRelation

data class PageWithBoxesItem(
    val page: PageItem,
    val boxes: List<BoxItem>
)

fun PageWithBoxesRelation.toDomain(): PageWithBoxesItem {
    return PageWithBoxesItem(page.toDomain(), boxes.map { it.toDomain() })
}