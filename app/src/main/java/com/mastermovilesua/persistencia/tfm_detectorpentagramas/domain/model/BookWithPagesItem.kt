package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

data class BookWithPagesItem (
    override val id: ID,
    val book: BookItem,
    val pages: List<PageItem>
) : IdentifiableItem
