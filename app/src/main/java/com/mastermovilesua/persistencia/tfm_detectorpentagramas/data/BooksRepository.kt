package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.MusicScoreBooksDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.BookWithPagesRelation
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.toDomain
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val musicScoreBooksDao: MusicScoreBooksDao
) {

    suspend fun getAllBooks(): List<BookItem> {
        return musicScoreBooksDao.getAllBooks().map { it.toDomain() }
    }

    suspend fun getBookWithPages(bookId: Int): List<BookWithPagesRelation> {
        return musicScoreBooksDao.getBookWithPages(bookId)
    }
}