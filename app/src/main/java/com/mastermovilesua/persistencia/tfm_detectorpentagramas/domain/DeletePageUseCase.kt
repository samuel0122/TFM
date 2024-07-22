package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import javax.inject.Inject

class DeletePageUseCase @Inject constructor(
    private val repository: PageRepository
) {
    suspend operator fun invoke(pageId: Int): Boolean = repository.deletePage(pageId)
}