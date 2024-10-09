package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.contracts.PageApiContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model.PageWithBoxesModel
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model.StatusModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming

interface PageApiClient {
    @Multipart
    @POST(PageApiContract.POSTRequests.PROCESS_IMAGE)
    suspend fun uploadImage(
        @Part(PageApiContract.PAGE_FIELD_ID) pageId: RequestBody,
        @Part(PageApiContract.BOOK_FIELD_DATASET) dataset: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<PageWithBoxesModel>

    @GET(PageApiContract.GETRequests.STATUS)
    suspend fun getStatus(): Response<StatusModel>

    @GET(PageApiContract.GETRequests.DOWNLOADABLE_IMAGES)
    suspend fun downloadableImagesList(@Path(PageApiContract.GETRequests.DOWNLOADABLE_IMAGES_DATASET_PATH) dataset: String): Response<List<String>>

    @Streaming
    @GET(PageApiContract.GETRequests.DOWNLOAD_IMAGE)
    suspend fun downloadImage(
        @Path(PageApiContract.GETRequests.DOWNLOAD_IMAGE_DATASET_PATH) dataset: String,
        @Path(PageApiContract.GETRequests.DOWNLOAD_IMAGE_FILE_PATH) file: String
    ): Response<ResponseBody>
}
