package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

data class BookItem (
    override val id: ID = 0,
    var title: String,
    var description: String,
    var dataset: Dataset = Dataset.Capitan,
): IdentifiableItem
