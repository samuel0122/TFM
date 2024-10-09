package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.contracts.PageApiContract
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model.PageWithBoxesModel
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model.StatusModel
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
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

    suspend fun getDownloadableImagesList(dataset: String): List<String>? {
        val response = api.downloadableImagesList(dataset.lowercase())

        return if (response.isSuccessful) response.body()
        else null
    }

    suspend fun downloadImage(dataset: String, file: String): ByteArray? {
        return try {
            withContext(Dispatchers.IO) {
                val response = api.downloadImage(dataset.lowercase(), file)

                if (response.isSuccessful) response.body()?.bytes()
                else {
                    Log.e(TAG, "Error HTTP: ${response.code()}")
                    null
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red o IO: ${e.message}")
            null
        }
    }

    private companion object {
        private const val TAG = "PageService"
    }
}