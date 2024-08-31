package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.PageEntity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.relations.PageWithBoxesRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface PageDao {
    @Transaction
    suspend fun deletePageAndReorder(pageId: Int): Boolean {
        val removingPageBook = getPage(pageId)

        val pageDeleted = deletePage(pageId) > 0

        removingPageBook?.let {
            val pages = getOrderedBookPages(removingPageBook.bookId).mapIndexed { index, pageEntity ->
                pageEntity.apply { order = index }
            }
            updatePages(pages)
        }

        return pageDeleted
    }

    /**
     * @return List with all PageEntity.
     */
    @Query("SELECT * FROM ${MusicScoreBooksContract.TABLE_PAGE}")
    suspend fun getAllPages(): List<PageEntity>

    /**
     * @return PageEntity if exists.
     */
    @Query(
        "SELECT * FROM ${MusicScoreBooksContract.TABLE_PAGE} " +
                "WHERE ${MusicScoreBooksContract.TABLE_PAGE_COLUMN_PAGE_ID} = :pageId"
    )
    suspend fun getPage(pageId: Int): PageEntity?

    /**
     * @return PageWithBoxesRelation that contains the PageEntity with a list of related BoxEntity.
     */
    @Transaction
    @Query(
        "SELECT * FROM ${MusicScoreBooksContract.TABLE_PAGE} " +
                "WHERE ${MusicScoreBooksContract.TABLE_PAGE_COLUMN_PAGE_ID} = :pageId"
    )
    fun getPageWithBoxes(pageId: Int): Flow<PageWithBoxesRelation?>

    @Query(
        "SELECT * FROM ${MusicScoreBooksContract.TABLE_PAGE} " +
                "WHERE ${MusicScoreBooksContract.TABLE_PAGE_COLUMN_BOOK_ID} = :bookId " +
                "ORDER BY `${MusicScoreBooksContract.TABLE_PAGE_COLUMN_ORDER}` ASC"
    )
    suspend fun getOrderedBookPages(bookId: Int): List<PageEntity>

    @Query(
        "SELECT * FROM ${MusicScoreBooksContract.TABLE_PAGE} " +
                "WHERE ${MusicScoreBooksContract.TABLE_PAGE_COLUMN_BOOK_ID} = :bookId " +
                "ORDER BY `${MusicScoreBooksContract.TABLE_PAGE_COLUMN_ORDER}` DESC " +
                ""//"LIMIT 1"
    )
    suspend fun getLastPageOfBook(bookId: Int): List<PageEntity>

    /**
     * @return ID of inserted or replaced PageEntity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: PageEntity): Long

    /**
     * @return Number of entities affected.
     */
    @Update
    suspend fun updatePage(page: PageEntity): Int

    @Transaction
    @Update
    suspend fun updatePages(pages: List<PageEntity>): Int

    /**
     * @return Number of entities affected.
     */
    @Query(
        "DELETE FROM ${MusicScoreBooksContract.TABLE_PAGE} " +
                "WHERE ${MusicScoreBooksContract.TABLE_PAGE_COLUMN_PAGE_ID} = :pageId"
    )
    suspend fun deletePage(pageId: Int): Int
}