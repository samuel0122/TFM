package com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network

import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.network.model.PageWithBoxesModel
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import okhttp3.MultipartBody
class PageService @Inject constructor(
    private val api: PageApiClient
) {
    suspend fun getBoxes(pageItem: PageItem): PageWithBoxesModel? {
        return withContext(Dispatchers.IO) {

            val imageFile = File(pageItem.imageUri)

            val idRequestBody = RequestBody.create(MediaType.get("text/plain"), pageItem.pageId.toString())

            val requestFile = RequestBody.create(MediaType.get("image/jpeg"), imageFile)
            val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val response = api.uploadImage(idRequestBody, body)

            response.body()
        }
    }
}