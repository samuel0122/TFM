package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BoxRepository
import javax.inject.Inject

class DeleteBoxUseCase @Inject constructor(
    private val repository: BoxRepository
) {
    suspend operator fun invoke(boxId: Int): Boolean
            = repository.deleteBox(boxId)
}