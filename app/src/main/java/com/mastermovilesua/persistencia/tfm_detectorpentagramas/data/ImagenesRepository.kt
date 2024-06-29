package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data

import android.util.Log
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.ImagenesCargadasDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.ImagenesCargadas
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
        val imagen = imagenesDao.getImageById(id)

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

    suspend fun testWithImagenes() {
        val imagenes = imagenesDao.getAllImagenesCargadas()

        val firstImagen = imagenes.first()

        firstImagen.dirImagen = "HOLA MUNDO"

        val updateResult = imagenesDao.updateImagen(firstImagen)
        Log.e("TESTWITHIMAGENES", "UpdateResult: $updateResult")

        val updatedImagen = imagenesDao.getImageById(firstImagen.id)
        Log.e("TESTWITHIMAGENES", "UpdatedImagen: $updatedImagen")

        val deleteResult = imagenesDao.deleteById(firstImagen.id)
        Log.e("TESTWITHIMAGENES", "DeleteResult: $deleteResult")

        val deletedImagen = imagenesDao.getImageById(firstImagen.id)
        Log.e("TESTWITHIMAGENES", "DeletedImagen: $deletedImagen")

        val newImagen = ImagenesCargadas(dirImagen = "NUEA IMAGEN")
        Log.e("TESTWITHIMAGENES", "NewImagen: $newImagen")

        val insertResult = imagenesDao.insertImagen(newImagen)
        Log.e("TESTWITHIMAGENES", "InsertResult: $insertResult")

        // val insertedImagen = imagenesDao.getImageById(newImagen.id)
        val deleteAllResult = imagenesDao.deleteAll()
        Log.e("TESTWITHIMAGENES", "DeleteAllResult: $deleteAllResult")
    }
}