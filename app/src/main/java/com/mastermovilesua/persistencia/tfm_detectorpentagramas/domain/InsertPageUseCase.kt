package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import javax.inject.Inject

class InsertPageUseCase @Inject constructor(
    private val repository: PageRepository
) {
    suspend operator fun invoke(bookId: Int, page: PageItem): Int =
        repository.insertPage(bookId, page)
}