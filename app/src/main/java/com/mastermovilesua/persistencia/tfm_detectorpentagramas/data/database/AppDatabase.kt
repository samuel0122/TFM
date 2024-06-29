package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.BookDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.BoxDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.ImagenesCargadasDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.PageDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BookEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BoxEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.ImagenesCargadas
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.PageEntity

@Database(
    version = MusicScoreBooksContract.DATABASE_VERSION,
    entities = [
        BookEntity::class,
        PageEntity::class,
        BoxEntity::class,
        ImagenesCargadas::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imagenesCargadasDao(): ImagenesCargadasDao
    abstract fun bookDao(): BookDao
    abstract fun pageDao(): PageDao
    abstract fun boxDao(): BoxDao
}