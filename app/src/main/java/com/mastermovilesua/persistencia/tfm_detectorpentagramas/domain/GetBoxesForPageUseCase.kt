package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BoxRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBoxesForPageUseCase @Inject constructor(
    private val repository: BoxRepository
) {
    operator fun invoke(pageId: Int): Flow<List<BoxItem>> = repository.getBoxesForPage(pageId)
}