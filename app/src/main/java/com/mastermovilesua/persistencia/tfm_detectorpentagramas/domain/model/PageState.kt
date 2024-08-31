package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

enum class PageState(val value: Int) {
    NotProcessed(0),
    Processing(1),
    FailedToProcess(2),
    Processed(3);

    companion object {
        fun fromInt(value: Int): PageState {
            return PageState.entries.find { it.value == value } ?: NotProcessed
        }
    }
}