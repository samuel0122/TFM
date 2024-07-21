package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.PageEntity

data class PageItem(
    override val id: ID = 0,
    val imageUri: String,
    var processed: Boolean = false,
    var order: Int = 0,
): IdentifiableItem

fun PageEntity.toDomain() = PageItem(pageId, imageUri, processed, order)