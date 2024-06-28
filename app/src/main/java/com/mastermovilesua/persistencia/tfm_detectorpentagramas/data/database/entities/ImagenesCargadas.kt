package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.ImagenesCargadasContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ImagenesItem


@Entity(tableName = ImagenesCargadasContract.TABLE_IMAGENES_CARGADAS)
data class ImagenesCargadas  (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ImagenesCargadasContract.COLUMN_ID)
    val id: Int = 0,

    @ColumnInfo(name = ImagenesCargadasContract.COLUMN_DIR_IMAGEN)
    var dirImagen: String
)

fun ImagenesItem.toDatabase() = ImagenesCargadas(id, dirImagen)