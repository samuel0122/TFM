package com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model

data class BoxItem(
    override val id: ID = 0,
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float,
): IdentifiableItem
