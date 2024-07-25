package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import javax.inject.Inject

class GetPageUseCase @Inject constructor(
    private val repository: PageRepository
) {
    suspend operator fun invoke(pageId: Int): PageItem? = repository.getPage(pageId)
}