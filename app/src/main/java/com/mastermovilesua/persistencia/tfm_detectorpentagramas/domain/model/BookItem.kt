package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.Book


data class BookItem (
    val bookId: Int,
    val title: String,
    var description: String,
    var dataset: Int,
)

fun Book.toDomain() = BookItem(bookId, title, description, dataset)