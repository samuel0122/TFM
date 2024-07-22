package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BoxRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ID
import javax.inject.Inject

class DeleteBoxUseCase @Inject constructor(
    private val repository: BoxRepository
) {
    suspend operator fun invoke(boxId: ID): Boolean = repository.deleteBox(boxId)
}