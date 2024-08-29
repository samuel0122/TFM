package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import android.content.Context
import androidx.core.net.toUri
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import javax.inject.Inject

class DeletePageUseCase @Inject constructor(
    private val repository: PageRepository,
    private val context: Context
) {
    suspend operator fun invoke(pageId: Int): Boolean {
        val bookToDelete = repository.getPage(pageId) ?: return false

        val deleted = repository.deletePage(pageId)

        SaveToMediaStore.deleteImage(context, bookToDelete.imageUri.toUri())

        return deleted
    }
}