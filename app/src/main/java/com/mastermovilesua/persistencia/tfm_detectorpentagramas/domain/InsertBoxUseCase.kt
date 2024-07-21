package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BoxRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BoxItem
import javax.inject.Inject

class InsertBoxUseCase @Inject constructor(
    private val repository: BoxRepository
) {
    suspend operator fun invoke(pageId: Int, box: BoxItem): Int
            = repository.insertBox(pageId, box)
}