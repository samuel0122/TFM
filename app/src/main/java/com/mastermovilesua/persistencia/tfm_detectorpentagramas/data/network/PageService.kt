package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network

import android.content.Context
import androidx.core.net.toUri
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.contracts.PageApiContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model.PageWithBoxesModel
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model.StatusModel
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PageService @Inject constructor(
    private val api: PageApiClient,
    private val context: Context
) {
    suspend fun getBoxes(bookDataset: Int, pageItem: PageItem): PageWithBoxesModel? {
        val imageFile = SaveToMediaStore.loadTemporalFile(context, pageItem.imageUri.toUri())

        val idRequestBody = RequestBody.create(MediaType.get("text/plain"), pageItem.id.toString())
        val datasetRequestBody =
            RequestBody.create(MediaType.get("text/plain"), bookDataset.toString())

        val requestFile = RequestBody.create(MediaType.get("image/jpeg"), imageFile)
        val body = MultipartBody.Part.createFormData(
            PageApiContract.PAGE_FIELD_IMAGE,
            imageFile.name,
            requestFile
        )

        val response = api.uploadImage(idRequestBody, datasetRequestBody, body)

        return if (response.isSuccessful) response.body()
        else null
    }

    suspend fun status(): StatusModel? = api.getStatus().body()
}