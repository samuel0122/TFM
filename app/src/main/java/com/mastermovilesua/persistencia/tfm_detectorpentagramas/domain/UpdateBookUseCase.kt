package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import javax.inject.Inject

class UpdateBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(book: BookItem): Boolean
            = repository.updateBook(book)
}