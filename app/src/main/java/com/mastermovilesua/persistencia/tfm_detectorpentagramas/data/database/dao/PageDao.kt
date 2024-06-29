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

@Dao
interface PageDao {
    /**
     * @return List with all PageEntity.
     */
    @Query("SELECT * FROM ${MusicScoreBooksContract.TABLE_PAGE}")
    suspend fun getAllPages(): List<PageEntity>

    /**
     * @return PageEntity if exists.
     */
    @Query("SELECT * FROM ${MusicScoreBooksContract.TABLE_PAGE} " +
            "WHERE ${MusicScoreBooksContract.TABLE_PAGE_COLUMN_PAGE_ID} = :pageId")
    suspend fun getPage(pageId: Int): PageEntity?

    /**
     * @return PageWithBoxesRelation that contains the PageEntity with a list of related BoxEntity.
     */
    @Transaction
    @Query("SELECT * FROM ${MusicScoreBooksContract.TABLE_PAGE} " +
            "WHERE ${MusicScoreBooksContract.TABLE_PAGE_COLUMN_PAGE_ID} = :pageId")
    suspend fun getPageWithBoxes(pageId: Int): PageWithBoxesRelation?

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

    /**
     * @return Number of entities affected.
     */
    @Query("DELETE FROM ${MusicScoreBooksContract.TABLE_PAGE} " +
            "WHERE ${MusicScoreBooksContract.TABLE_PAGE_COLUMN_PAGE_ID} = :pageId")
    suspend fun deletePage(pageId: Int): Int
}