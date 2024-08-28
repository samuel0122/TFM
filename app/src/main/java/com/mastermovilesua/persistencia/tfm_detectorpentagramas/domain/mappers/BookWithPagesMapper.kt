package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.mappers

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.BookWithPagesRelation
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookWithPagesItem

fun BookWithPagesRelation.toDomain() = BookWithPagesItem(book.toDomain(), pages.sortedBy { it.order }.map { it.toDomain() })
