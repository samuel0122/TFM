package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.mappers

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BoxEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model.BoxModel
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem

fun BoxEntity.toDomain() = BoxItem(boxId, x, y, width, height)

fun BoxItem.toDatabase(pageId: Int) = BoxEntity(id, x, y, width, height, pageId)

fun BoxModel.toDomain() = BoxItem(x = x, y = y, width = width, height = height)