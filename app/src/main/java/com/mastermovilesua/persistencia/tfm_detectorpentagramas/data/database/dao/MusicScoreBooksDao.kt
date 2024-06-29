package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BookEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BoxEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.PageEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.BookWithPagesRelation
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.PageWithBoxes

@Dao
interface MusicScoreBooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(bookEntity: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: PageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBox(box: BoxEntity)

    @Query("SELECT * FROM Book")
    suspend fun getAllBooks(): List<BookEntity>

    @Transaction
    @Query("SELECT * FROM Book WHERE bookId = :bookId")
    suspend fun getBookWithPages(bookId: Int): List<BookWithPagesRelation>

    @Transaction
    @Query("SELECT * FROM Page WHERE pageId = :pageId")
    suspend fun getPageWithBoxes(pageId: Int): List<PageWithBoxes>
}