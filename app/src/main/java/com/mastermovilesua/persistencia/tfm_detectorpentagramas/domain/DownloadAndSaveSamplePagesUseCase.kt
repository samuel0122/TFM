package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import javax.inject.Inject

class DownloadAndSaveSamplePagesUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val downloadSamplePagesUseCase: DownloadSamplePagesUseCase,
    private val insertPageUseCase: InsertPageUseCase,
) {

    suspend operator fun invoke(bookId: Int): Boolean {
        val bookDataset = bookRepository.getBook(bookId)?.dataset ?: return true

        return downloadSamplePagesUseCase(bookId)?.let { downloadedImages ->
            downloadedImages.forEach { insertPageUseCase(bookId, bookDataset.name, it) }

            true
        } ?: false
    }
}