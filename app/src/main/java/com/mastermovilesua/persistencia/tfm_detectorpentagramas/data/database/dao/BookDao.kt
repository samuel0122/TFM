package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BookEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.BookWithPagesRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    /**
     * @return List with all BookEntity.
     */
    @Query("SELECT * FROM ${MusicScoreBooksContract.TABLE_BOOK}")
    fun getAllBooks(): Flow<List<BookEntity>>

    /**
     * @return BookEntity if exists.
     */
    @Query("SELECT * FROM ${MusicScoreBooksContract.TABLE_BOOK} " +
            "WHERE ${MusicScoreBooksContract.TABLE_BOOK_COLUMN_BOOK_ID} = :bookId")
    suspend fun getBook(bookId: Int): BookEntity?

    /**
     * @return BookWithPagesRelation that contains the BookEntity with a list of related PageEntity.
     */
    @Transaction
    @Query("SELECT * FROM ${MusicScoreBooksContract.TABLE_BOOK} " +
            "WHERE ${MusicScoreBooksContract.TABLE_BOOK_COLUMN_BOOK_ID} = :bookId")
    fun getBookWithPages(bookId: Int): Flow<BookWithPagesRelation>

    /**
     * @return ID of inserted or replaced BookEntity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity): Long

    /**
     * @return Number of entities affected.
     */
    @Update
    suspend fun updateBook(book: BookEntity): Int

    /**
     * @return Number of entities affected.
     */
    @Query("DELETE FROM ${MusicScoreBooksContract.TABLE_BOOK} " +
            "WHERE ${MusicScoreBooksContract.TABLE_BOOK_COLUMN_BOOK_ID} = :bookId")
    suspend fun deleteBook(bookId: Int): Int
}