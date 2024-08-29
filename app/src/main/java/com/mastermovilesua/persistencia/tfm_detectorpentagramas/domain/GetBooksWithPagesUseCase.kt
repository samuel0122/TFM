package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookWithPagesItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBooksWithPagesUseCase @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(): Flow<List<BookWithPagesItem>> = repository.getAllBooksWithPages()
}