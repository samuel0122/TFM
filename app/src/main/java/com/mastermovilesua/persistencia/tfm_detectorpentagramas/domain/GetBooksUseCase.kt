package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BooksRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import javax.inject.Inject


class GetBooksUseCase @Inject constructor(
    private val repository: BooksRepository
){
    suspend operator fun invoke(): List<BookItem> = repository.getAllBooks()
}