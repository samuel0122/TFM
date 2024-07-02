package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookWithPagesItem
import javax.inject.Inject

class GetBookWithPagesUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId: Int): BookWithPagesItem?
    = repository.getBookWithPages(bookId)
}