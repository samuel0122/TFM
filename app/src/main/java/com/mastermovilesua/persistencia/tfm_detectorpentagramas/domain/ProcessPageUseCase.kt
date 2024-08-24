package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BoxRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import javax.inject.Inject

class ProcessPageUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val pageRepository: PageRepository,
    private val boxRepository: BoxRepository
) {
    suspend operator fun invoke(pageId: Int): Boolean {
        pageRepository.getPage(pageId)?.let { page ->
            if(!page.processed) {
                bookRepository.getBookOfPage(pageId)?.let { book ->
                    pageRepository.getProcessedPageBoxes(book.id, pageId)?.let { boxes ->
                        boxes.map { box -> boxRepository.insertBox(pageId, box) }
                        return true
                    }
                }
            }
        }

        return false
    }
}