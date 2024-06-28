package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.ImagenesRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ImagenesItem
import javax.inject.Inject

class UpdateImagenUseCase @Inject constructor(
    private val repository: ImagenesRepository
) {
    suspend operator fun invoke(imagen: ImagenesItem) {
        repository.updateImagen(imagen)
    }
}