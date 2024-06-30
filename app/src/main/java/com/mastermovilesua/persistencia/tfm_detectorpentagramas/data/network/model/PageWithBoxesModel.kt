package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model

import com.google.gson.annotations.SerializedName
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.contracts.PageApiContract

data class PageWithBoxesModel (
    @SerializedName(PageApiContract.PAGE_FIELD_ID) val pageId: Int,
    @SerializedName(PageApiContract.PAGE_FIELD_BOXES) val boxes: List<BoxModel>
)