package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.BookDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.toDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookWithPagesItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.toDomain
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {
    suspend fun getAllBooks(): List<BookItem> {
        return bookDao.getAllBooks().map { it.toDomain() }
    }

    suspend fun getBookWithPages(bookId: Int): BookWithPagesItem? {
        return bookDao.getBookWithPages(bookId)?.toDomain()
    }

    suspend fun insertBook(bookItem: BookItem): Boolean {
        return bookDao.insertBook(bookItem.toDatabase()) > 0
    }

    suspend fun updateBook(bookItem: BookItem): Boolean {
        return bookDao.updateBook(bookItem.toDatabase()) > 0
    }

    suspend fun deleteBook(bookId: Int): Boolean {
        return bookDao.deleteBook(bookId) > 0
    }
}