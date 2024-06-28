package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.ImagenesCargadas

data class ImagenesItem (
    val id: Int,
    val dirImagen: String
)

fun ImagenesCargadas.toDomain() = ImagenesItem(id, dirImagen)