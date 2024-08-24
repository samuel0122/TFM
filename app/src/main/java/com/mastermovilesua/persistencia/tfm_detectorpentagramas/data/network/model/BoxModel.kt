package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model

import com.google.gson.annotations.SerializedName
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.contracts.PageApiContract

data class BoxModel (
    @SerializedName(PageApiContract.BOX_FIELD_X) val x: Float,
    @SerializedName(PageApiContract.BOX_FIELD_Y) val y: Float,
    @SerializedName(PageApiContract.BOX_FIELD_WIDTH) val width: Float,
    @SerializedName(PageApiContract.BOX_FIELD_HEIGHT) val height: Float
)