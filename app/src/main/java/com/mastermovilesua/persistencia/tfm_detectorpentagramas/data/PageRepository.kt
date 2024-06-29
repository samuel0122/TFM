package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.BookDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.PageDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.toDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookWithPagesItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageWithBoxesItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.toDomain
import javax.inject.Inject

class PageRepository @Inject constructor(
    private val pageDao: PageDao
) {
    suspend fun getAllPages(): List<PageItem> {
        return pageDao.getAllPages().map { it.toDomain() }
    }

    suspend fun getPageWithBoxes(pageId: Int): PageWithBoxesItem? {
        return pageDao.getPageWithBoxes(pageId)?.toDomain()
    }

    suspend fun insertPage(bookId: Int, pageItem: PageItem): Boolean {
        return pageDao.insertPage(pageItem.toDatabase(bookId)) > 0
    }

    suspend fun updatePage(pageItem: PageItem): Boolean {
        return pageDao.getPage(pageItem.pageId)?.let {
            pageDao.updatePage(pageItem.toDatabase(it.bookId)) > 0
        } ?: false
    }

    suspend fun deletePage(pageId: Int): Boolean {
        return pageDao.deletePage(pageId) > 0
    }
}