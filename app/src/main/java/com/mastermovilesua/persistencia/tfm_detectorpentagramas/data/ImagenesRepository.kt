package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.ImagenesCargadasDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.toDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ImagenesItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.toDomain
import javax.inject.Inject

class ImagenesRepository  @Inject constructor(
    private val imagenesDao: ImagenesCargadasDao
){
    suspend fun getAllImagenesFromDatabase(): List<ImagenesItem> {
        return imagenesDao.getAllImagenesCargadas().map { it.toDomain() }
    }

    suspend fun getImagenFromDatabase(id: Int): ImagenesItem {
        val imagen = imagenesDao.getImageAtPosition(id)

        return imagen?.toDomain() ?: ImagenesItem(0, "")
    }

    suspend fun insertImagenToDatabase(imagen: ImagenesItem) {
        imagenesDao.insertImagen(imagen.toDatabase())
    }

    suspend fun updateImagen(imagen: ImagenesItem) {
        imagenesDao.updateImagen(imagen.toDatabase())
    }

    suspend fun deleteImagen(id: Int) {
        imagenesDao.deleteById(id)
    }
}