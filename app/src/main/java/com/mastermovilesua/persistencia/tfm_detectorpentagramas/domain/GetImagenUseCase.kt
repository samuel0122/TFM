package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.ImagenesRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ImagenesItem
import javax.inject.Inject

class GetImagenUseCase @Inject constructor(
    private val repository: ImagenesRepository
) {
    suspend operator fun invoke(id: Int): ImagenesItem {
        return repository.getImagenFromDatabase(id)
    }
}