package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.BoxDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.mappers.toDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.mappers.toDomain
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BoxRepository @Inject constructor(
    private val boxDao: BoxDao
) {
    suspend fun getAllBoxes(): List<BoxItem> {
        return boxDao.getAllBoxes().map { it.toDomain() }
    }

    fun getBoxesForPage(pageId: Int): Flow<List<BoxItem>> {
        return boxDao.getBoxesForPage(pageId).map { boxes ->
            boxes.map { it.toDomain() }
        }
    }

    /**
     * @return ID of inserted Box.
     */
    suspend fun insertBox(pageId: Int, boxItem: BoxItem): Int {
        return boxDao.insertBox(boxItem.toDatabase(pageId)).toInt()
    }

    suspend fun updateBox(boxItem: BoxItem): Boolean {
        return boxDao.getBox(boxItem.id)?.let {
            boxDao.updateBox(boxItem.toDatabase(it.pageId)) > 0
        } ?: false
    }

    suspend fun deleteBoxesFromPage(pageId: Int): Int {
        return boxDao.deleteBoxesFromPage(pageId)
    }

    suspend fun deleteBox(boxId: Int): Boolean {
        return boxDao.deleteBox(boxId) > 0
    }
}