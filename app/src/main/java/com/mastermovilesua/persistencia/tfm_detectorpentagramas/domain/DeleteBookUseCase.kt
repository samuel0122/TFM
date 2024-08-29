package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import android.content.Context
import androidx.core.net.toUri
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(
    private val repository: BookRepository,
    private val pageRepository: PageRepository,
    private val context: Context
) {
    suspend operator fun invoke(bookId: Int): Boolean {
        val bookPages = pageRepository.getOrderedBookPages(bookId)

        val deleted = repository.deleteBook(bookId)

        bookPages.forEach { page ->
            SaveToMediaStore.deleteImage(context, page.imageUri.toUri())
        }

        return deleted
    }
}