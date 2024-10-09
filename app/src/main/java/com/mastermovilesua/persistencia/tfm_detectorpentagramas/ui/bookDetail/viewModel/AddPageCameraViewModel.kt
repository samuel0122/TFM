package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.bookDetail.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.RequestProcessPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.camera.CameraViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPageCameraViewModel @Inject constructor(
    private val insertPageUseCase: InsertPageUseCase,
    private val requestProcessPageUseCase: RequestProcessPageUseCase,
) : CameraViewModel() {

    private var bookId: Int = 0

    private val _didInsertPage = MutableLiveData<Boolean>()

    val didInsertPage: LiveData<Boolean> get() = _didInsertPage

    fun onCreate(bookId: Int) {
        this.bookId = bookId
        super.onCreate()
        _didInsertPage.postValue(false)
    }

    fun insertCapturedPage() {
        viewModelScope.launch {
            pictureBitmap.value?.let { pictureBitmap ->
                val insertedPageId =
                    insertPageUseCase(bookId, "CapturedImage", pictureBitmap)

                if (insertedPageId > 0) {
                    requestProcessPageUseCase(insertedPageId)
                    _didInsertPage.postValue(true)
                }
            }
        }
    }
}