package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

data class PageItem(
    override val id: ID = 0,
    val imageUri: String,
    var processed: Boolean = false,
    var order: Int = 0,
): IdentifiableItem
