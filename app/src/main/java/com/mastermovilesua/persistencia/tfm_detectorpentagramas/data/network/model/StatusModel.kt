package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model

import com.google.gson.annotations.SerializedName

data class StatusModel (
    @SerializedName("status") val status: String,
    @SerializedName("timestamp") val timestamp: String
)