package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

data class BookWithPagesItem(
    val book: BookItem,
    val pages: List<PageItem>
)
