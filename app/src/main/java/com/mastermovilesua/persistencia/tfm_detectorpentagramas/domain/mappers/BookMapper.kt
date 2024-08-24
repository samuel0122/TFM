package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.mappers

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BookEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.Dataset

fun BookEntity.toDomain() = BookItem(bookId, title, description, Dataset.fromInt(dataset))

fun BookItem.toDatabase() = BookEntity(id, title, description, dataset.value)
