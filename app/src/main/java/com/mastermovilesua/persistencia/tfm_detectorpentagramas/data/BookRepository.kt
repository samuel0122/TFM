package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.BookDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.toDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookWithPagesItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {
    suspend fun getAllBooks(): Flow<List<BookItem>> =
        bookDao.getAllBooks().map { booksList ->
            booksList.map { book -> book.toDomain() }
        }


    suspend fun getBook(bookId: Int): BookItem? =
        bookDao.getBook(bookId)?.toDomain()

    suspend fun getBookWithPages(bookId: Int): BookWithPagesItem? =
        bookDao.getBookWithPages(bookId)?.toDomain()

    /**
     * @return ID of inserted Book.
     */
    suspend fun insertBook(bookItem: BookItem): Int =
        bookDao.insertBook(bookItem.toDatabase()).toInt()

    suspend fun updateBook(bookItem: BookItem): Boolean =
        bookDao.updateBook(bookItem.toDatabase()) > 0

    suspend fun deleteBook(bookId: Int): Boolean =
        bookDao.deleteBook(bookId) > 0
}