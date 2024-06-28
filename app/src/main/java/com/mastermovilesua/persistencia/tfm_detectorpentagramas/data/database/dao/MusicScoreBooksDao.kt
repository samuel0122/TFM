package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.ImagenesCargadasContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.Book
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.Box
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.ImagenesCargadas
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.Page
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.BookWithPages
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.PageWithBoxes

@Dao
interface MusicScoreBooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: Page)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBox(box: Box)

    @Query("SELECT * FROM Book")
    suspend fun getAllBooks(): List<Book>

    @Transaction
    @Query("SELECT * FROM Book WHERE bookId = :bookId")
    suspend fun getBookWithPages(bookId: Int): List<BookWithPages>

    @Transaction
    @Query("SELECT * FROM Page WHERE pageId = :pageId")
    suspend fun getPageWithBoxes(pageId: Int): List<PageWithBoxes>
}