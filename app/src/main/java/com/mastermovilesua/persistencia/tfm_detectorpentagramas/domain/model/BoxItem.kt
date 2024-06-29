package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BoxEntity

data class BoxItem(
    val boxId: Int,
    var x: Int,
    var y: Int,
    var width: Int,
    var height: Int,
)

fun BoxEntity.toDomain() = BoxItem(boxId, x, y, width, height)