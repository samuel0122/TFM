package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.mappers

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.PageEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageState

fun PageEntity.toDomain() = PageItem(pageId, imageUri, PageState.fromInt(state), order)

fun PageItem.toDatabase(bookId: Int) = PageEntity(id, imageUri, processState.value, order, bookId)
