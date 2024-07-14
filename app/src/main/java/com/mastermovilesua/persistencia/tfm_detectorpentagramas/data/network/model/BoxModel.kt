package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model

import com.google.gson.annotations.SerializedName
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.contracts.PageApiContract

data class BoxModel (
    @SerializedName(PageApiContract.BOX_FIELD_X) val x: Int,
    @SerializedName(PageApiContract.BOX_FIELD_Y) val y: Int,
    @SerializedName(PageApiContract.BOX_FIELD_WIDTH) val width: Int,
    @SerializedName(PageApiContract.BOX_FIELD_HEIGHT) val height: Int
)