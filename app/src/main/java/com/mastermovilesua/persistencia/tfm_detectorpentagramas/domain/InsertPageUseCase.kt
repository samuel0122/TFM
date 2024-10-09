package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import android.content.Context
import android.graphics.Bitmap
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import javax.inject.Inject

class InsertPageUseCase @Inject constructor(
    private val repository: PageRepository,
    private val context: Context
) {
    suspend operator fun invoke(bookId: Int, page: PageItem): Int =
        repository.insertPage(bookId, page)

    suspend operator fun invoke(bookId: Int, fileName: String, image: Bitmap): Int {
        return SaveToMediaStore.saveImageToInternalStorage(context, image, fileName)
            ?.let { fileUri ->
                this(bookId, PageItem(imageUri = fileUri.toString()))
            } ?: 0
    }

    suspend operator fun invoke(bookId: Int, fileName: String, image: ByteArray): Int {
        return SaveToMediaStore.saveImageToInternalStorage(context, image, fileName)
            ?.let { fileUri ->
                this(bookId, PageItem(imageUri = fileUri.toString()))
            } ?: 0
    }
}