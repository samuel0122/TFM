package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.mappers

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.PageEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem

fun PageEntity.toDomain() = PageItem(pageId, imageUri, processed, order)

fun PageItem.toDatabase(bookId: Int) = PageEntity(id, imageUri, processed, order, bookId)
