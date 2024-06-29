package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.contracts.MusicScoreBooksContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.BoxEntity

@Dao
interface BoxDao {
    /**
     * @return List with all BoxEntity.
     */
    @Query("SELECT * FROM ${MusicScoreBooksContract.TABLE_BOX}")
    suspend fun getAllBoxes(): List<BoxEntity>

    /**
     * @return BoxEntity is exists.
     */
    @Query("SELECT * FROM ${MusicScoreBooksContract.TABLE_BOX} " +
            "WHERE ${MusicScoreBooksContract.TABLE_BOX_COLUMN_BOX_ID} = :boxId")
    suspend fun getBox(boxId: Int): BoxEntity?

    /**
     * @return ID of inserted or replaced BoxEntity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBox(box: BoxEntity): Long

    /**
     * @return Number of entities affected.
     */
    @Update
    suspend fun updateBox(box: BoxEntity): Int

    /**
     * @return Number of entities affected.
     */
    @Query("DELETE FROM ${MusicScoreBooksContract.TABLE_BOX} " +
            "WHERE ${MusicScoreBooksContract.TABLE_BOX_COLUMN_BOX_ID} = :boxId")
    suspend fun deleteBox(boxId: Int): Int
}