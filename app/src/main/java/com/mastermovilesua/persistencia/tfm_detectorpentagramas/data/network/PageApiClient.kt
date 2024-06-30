package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model.PageWithBoxesModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PageApiClient {
    @Multipart
    @POST("/api/v1/uploadimage")
    fun uploadImage(
        @Part("id") id: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<PageWithBoxesModel>
}