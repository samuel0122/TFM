package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import android.util.Log
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import javax.inject.Inject

class UpdatePageOrderUseCase @Inject constructor(
    private val repository: PageRepository,
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(pageId: Int, newOrder: Int): Boolean {
        val book = bookRepository.getBookOfPage(pageId) ?: return false

        val bookPages = repository.getOrderedBookPages(book.id).toMutableList()

        val pageToMove = bookPages.find { it.id == pageId } ?: return false

        bookPages.remove(pageToMove)

        bookPages.add(newOrder, pageToMove)

        bookPages.forEachIndexed { index, pageEntity ->
            pageEntity.order = index
        }

        return repository.updatePages(bookPages) == bookPages.size
    }
}