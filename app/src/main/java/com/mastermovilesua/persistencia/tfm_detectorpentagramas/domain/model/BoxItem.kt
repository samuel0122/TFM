package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BoxEntity

data class BoxItem(
    override val id: ID = 0,
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
): IdentifiableItem

fun BoxEntity.toDomain() = BoxItem(boxId, x, y, width, height)