package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.ImagenesCargadasDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.MusicScoreBooksDao
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

    abstract fun musicScoreBooksDao(): MusicScoreBooksDao
    abstract fun imagenesCargadasDao(): ImagenesCargadasDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder (
                    context.applicationContext,
                    AppDatabase::class.java,
                    MusicScoreBooksContract.DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }

            return instance as AppDatabase
        }
    }
}