package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.BookDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.PageDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.mappers.toDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.mappers.toDomain
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookWithPagesItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao,
    private val pageDao: PageDao
) {
    fun getAllBooks(): Flow<List<BookItem>> =
        bookDao.getAllBooks().map { booksList ->
            booksList.map { book -> book.toDomain() }
        }

    fun getAllBooksWithPages(): Flow<List<BookWithPagesItem>> =
        bookDao.getAllBooksWithPages().map { booksList ->
            booksList.map { book -> book.toDomain() }
        }


    suspend fun getBook(bookId: Int): BookItem? =
        bookDao.getBook(bookId)?.toDomain()

    suspend fun getBookOfPage(pageId: Int): BookItem? =
        pageDao.getPage(pageId)?.let { page ->
            bookDao.getBook(page.bookId)
        }?.toDomain()


    fun getBookWithPages(bookId: Int): Flow<BookWithPagesItem> =
        bookDao.getBookWithPages(bookId)
            .takeWhile { it != null }
            .map { bookWithPages -> bookWithPages!!.toDomain() }

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