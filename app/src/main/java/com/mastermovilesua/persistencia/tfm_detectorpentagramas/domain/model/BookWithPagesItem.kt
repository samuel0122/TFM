package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.BookWithPagesRelation

data class BookWithPagesItem(
    val book: BookItem,
    val pages: List<PageItem>
)

fun BookWithPagesRelation.toDomain() = BookWithPagesItem(book.toDomain(), pages.map { it.toDomain() })