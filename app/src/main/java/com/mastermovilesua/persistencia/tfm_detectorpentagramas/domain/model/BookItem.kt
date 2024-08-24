package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

enum class Dataset(val value: Int) {
    Capitan(0),
    SEILS(1),
    FMT_C(2);

    companion object {
        fun fromInt(value: Int): Dataset {
            return entries.find { it.value == value } ?: Capitan
        }
    }
}

data class BookItem (
    override val id: ID = 0,
    var title: String,
    var description: String,
    var dataset: Dataset = Dataset.Capitan,
): IdentifiableItem
