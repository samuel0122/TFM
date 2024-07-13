package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel

import android.content.Context
import android.net.Uri
import androidx.annotation.NonNull
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.MeteringPoint
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.eCameraFacing
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.eCameraState
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.camera.CameraViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
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