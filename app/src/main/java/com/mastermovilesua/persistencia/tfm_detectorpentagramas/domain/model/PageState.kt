package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

enum class PageState(val value: Int) {
    NotProcessed(0),
    WaitingForProcessing(1),
    Processing(2),
    FailedToProcess(3),
    Processed(4);

    companion object {
        fun fromInt(value: Int): PageState {
            return PageState.entries.find { it.value == value } ?: NotProcessed
        }
    }
}