package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import android.content.Context
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import javax.inject.Inject

class DownloadDefaultBookPagesUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val pageRepository: PageRepository,
    private val context: Context,
    private val insertPageUseCase: InsertPageUseCase
) {

    suspend operator fun invoke(bookId: Int): Boolean {
        bookRepository.getBook(bookId)?.let { book ->
            val pagesList = pageRepository.getDownloadableImagesList(book.dataset.name)

            pagesList?.forEach { pageFile ->
                pageRepository.downloadImage(book.dataset.name, pageFile)?.let { downloadedImage ->
                    SaveToMediaStore.saveImageToInternalStorage(
                        context,
                        downloadedImage,
                        book.dataset.name
                    )?.let { fileUri ->
                        insertPageUseCase(bookId, PageItem(imageUri = fileUri.toString()))
                    }
                }
            }
        }

        return true
    }
}