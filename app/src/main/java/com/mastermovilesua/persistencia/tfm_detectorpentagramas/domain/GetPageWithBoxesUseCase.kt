package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageWithBoxesItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPageWithBoxesUseCase @Inject constructor(
    private val repository: PageRepository
) {
    operator fun invoke(pageId: Int): Flow<PageWithBoxesItem> = repository.getPageWithBoxes(pageId)
}