package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.BookRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.PageRepository
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookWithPagesItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageWithBoxesItem
import javax.inject.Inject

class GetPageWithBoxesUseCase @Inject constructor(
    private val repository: PageRepository
)  {

    suspend operator fun invoke(pageId: Int): PageWithBoxesItem?
            = repository.getPageWithBoxes(pageId)
}