package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.mappers

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.PageWithBoxesRelation
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageWithBoxesItem

fun PageWithBoxesRelation.toDomain() = PageWithBoxesItem(page.toDomain(), boxes.map { it.toDomain() })
