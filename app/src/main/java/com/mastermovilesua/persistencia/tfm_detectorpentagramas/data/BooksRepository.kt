package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.ImagenesCargadasDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.MusicScoreBooksDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ImagenesItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.toDomain
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val musicScoreBooksDao: MusicScoreBooksDao
) {

    suspend fun getAllBooks(): List<BookItem> {
        return musicScoreBooksDao.getAllBooks().map { it.toDomain() }
    }

}