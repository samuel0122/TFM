package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import javax.inject.Inject

class DownloadSamplePagesUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val pageRepository: PageRepository
) {

    suspend operator fun invoke(bookId: Int): List<ByteArray>? {
        val bookDataset = bookRepository.getBook(bookId)?.dataset ?: return null

        val pagesList = pageRepository.getDownloadableImagesList(bookDataset.name)

        return pagesList?.mapNotNull { pageFile ->
            pageRepository.downloadImage(bookDataset.name, pageFile)
        }
    }
}