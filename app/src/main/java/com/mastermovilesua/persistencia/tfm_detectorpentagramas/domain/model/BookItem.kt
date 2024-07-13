package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BookEntity

enum class Datasets(val value: Int) {
    Capitan(0),
    SEILS(1),
    FMT_C(2);

    companion object {
        fun fromInt(value: Int): Datasets {
            return entries.find { it.value == value } ?: Capitan
        }
    }
}

data class BookItem (
    val bookId: Int = 0,
    var title: String,
    var description: String,
    var dataset: Datasets = Datasets.Capitan,
)

fun BookEntity.toDomain() = BookItem(bookId, title, description, Datasets.fromInt(dataset))