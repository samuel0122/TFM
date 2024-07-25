package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.PageDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.toDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageWithBoxesItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PageRepository @Inject constructor(
    private val pageDao: PageDao
) {
    suspend fun getAllPages(): List<PageItem> {
        return pageDao.getAllPages().map { it.toDomain() }
    }

    suspend fun getPage(pageId: Int) : PageItem? {
        return pageDao.getPage(pageId)?.toDomain()
    }

    fun getPageWithBoxes(pageId: Int): Flow<PageWithBoxesItem> {
        return pageDao.getPageWithBoxes(pageId).map {
            it.toDomain()
        }
    }

    /**
     * @return ID of inserted Page.
     */
    suspend fun insertPage(bookId: Int, pageItem: PageItem): Int {
        return pageDao.insertPage(pageItem.toDatabase(bookId)).toInt()
    }

    suspend fun updatePage(pageItem: PageItem): Boolean {
        return pageDao.getPage(pageItem.id)?.let {
            pageDao.updatePage(pageItem.toDatabase(it.bookId)) > 0
        } ?: false
    }

    suspend fun deletePage(pageId: Int): Boolean {
        return pageDao.deletePage(pageId) > 0
    }
}