package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ImagenesItem
import javax.inject.Inject

class InsertBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(book: BookItem) {
        repository.insertBook(book)
    }
}