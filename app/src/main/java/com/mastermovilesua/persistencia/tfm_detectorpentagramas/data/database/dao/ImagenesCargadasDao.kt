package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.ImagenesCargadas
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.ImagenesCargadasContract

@Dao
interface ImagenesCargadasDao {

    @Query("SELECT * FROM ${ImagenesCargadasContract.TABLE_IMAGENES_CARGADAS}")
    suspend fun getAllImagenesCargadas(): List<ImagenesCargadas>

    // INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImagen(imagen: ImagenesCargadas)

    // UPDATE
    @Update
    suspend fun updateImagen(imagen: ImagenesCargadas)

    @Query("UPDATE ${ImagenesCargadasContract.TABLE_IMAGENES_CARGADAS} " +
            "SET ${ImagenesCargadasContract.COLUMN_DIR_IMAGEN} = :pDirImagen " +
            "WHERE ${ImagenesCargadasContract.COLUMN_ID} = :pId")
    suspend fun updateById(pId: Int, pDirImagen: String): Int

    // DELETE
    @Delete
    suspend fun deleteImagen(imagen: ImagenesCargadas)

    @Query("DELETE FROM ${ImagenesCargadasContract.TABLE_IMAGENES_CARGADAS} " +
            "WHERE ${ImagenesCargadasContract.COLUMN_ID} = :pId")
    suspend fun deleteById(pId: Int): Int

    // Cantidad de imágenes
    @Query("SELECT COUNT(*) FROM ${ImagenesCargadasContract.TABLE_IMAGENES_CARGADAS}")
    suspend fun getRowCount(): Int

    // Imagen en la posicion indicada
    @Query("SELECT * FROM ${ImagenesCargadasContract.TABLE_IMAGENES_CARGADAS} " +
            "WHERE ${ImagenesCargadasContract.COLUMN_ID} = :pId")
    suspend fun getImageById(pId: Int): ImagenesCargadas?
}