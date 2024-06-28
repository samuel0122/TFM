package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts

import android.provider.BaseColumns

object ImagenesCargadasContract: BaseColumns {
    // Constantes para la base de datos
    const val DATABASE_NAME = "databaseTFM7.db"
    const val DATABASE_VERSION = 1

    // Constantes para la tabla Usuarios
    const val TABLE_IMAGENES_CARGADAS = "ImagenesCargadas"
    const val COLUMN_ID = BaseColumns._ID
    const val COLUMN_DIR_IMAGEN = "dir_imagen"

    const val AUTHORITY = "com.mastermovilesua.persistencia.tfm_detectorpentagramas"
}