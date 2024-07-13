package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.camera.CameraViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPageCameraViewModel @Inject constructor(
    private val insertPageUseCase: InsertPageUseCase
) : CameraViewModel() {

    private var bookId: Int = 0

    fun onCreate(bookId: Int) {
        this.bookId = bookId
        super.onCreate()
    }

    fun insertCapturedPage() {
        viewModelScope.launch {
            pictureUri.value?.let { pictureUri ->
                insertPageUseCase(bookId, PageItem(imageUri = pictureUri.toString()))
            }
        }
    }
}