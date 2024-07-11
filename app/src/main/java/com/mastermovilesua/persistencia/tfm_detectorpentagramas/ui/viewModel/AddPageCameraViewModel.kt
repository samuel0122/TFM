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
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.InsertPageUseCase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AddPageCameraViewModel  @Inject constructor(
    private val insertPageUseCase: InsertPageUseCase
) : ViewModel() {
    private val _isFlashOn = MutableLiveData<Boolean>()
    private val _didFlipCamera = MutableLiveData<Boolean>()
    private val _didTakePicture = MutableLiveData<Uri>()

    val didToggleFlash: LiveData<Boolean> get() = _isFlashOn
    val didFlipCamera: LiveData<Boolean> get() = _didFlipCamera
    val didTakePicture: LiveData<Uri> get() = _didTakePicture

    private var _cameraProvider: ProcessCameraProvider? = null
    private var _camera: Camera? = null

    val camera: Camera? get() = _camera
    val cameraProvider : ProcessCameraProvider? get() = _cameraProvider

    lateinit var preview : Preview
    lateinit var imageCapture: ImageCapture
    lateinit var cameraSelector: CameraSelector

    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var aspectRatio = AspectRatio.RATIO_16_9

    private lateinit var cameraExecutor: ExecutorService

    fun onCreate(rotation: Int) {
        cameraExecutor = Executors.newSingleThreadExecutor()

        buildImageCapture(rotation)

        buildPreview(rotation)

        buildCameraSelector()
    }

    fun onDestroy() {
        cameraExecutor.shutdown()
        cameraProvider?.unbindAll()
    }

    private fun buildPreview(rotation: Int) {
        val resolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(
                AspectRatioStrategy(
                    aspectRatio,
                    AspectRatioStrategy.FALLBACK_RULE_AUTO
                )
            )
            .build()

        preview = Preview.Builder()
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()
    }

    private fun buildImageCapture(rotation: Int) {
        val resolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(
                AspectRatioStrategy(
                    aspectRatio,
                    AspectRatioStrategy.FALLBACK_RULE_AUTO
                )
            )
            .build()

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()
    }

    private fun buildCameraSelector() {
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
    }

    fun insertPage(page: PageItem, bookId: Int) {
        viewModelScope.launch {
            insertPageUseCase(bookId, page)
        }
    }

    fun setCameraProvider(cameraProvider: ProcessCameraProvider) {
        _cameraProvider = cameraProvider
    }

    fun setCamera(camera: Camera) {
        _camera = camera
    }

    fun flipCamera() {
        if (lensFacing == CameraSelector.LENS_FACING_FRONT) lensFacing = CameraSelector.LENS_FACING_BACK
        else if (lensFacing == CameraSelector.LENS_FACING_BACK) lensFacing = CameraSelector.LENS_FACING_FRONT

        buildCameraSelector()

        _didFlipCamera.postValue(true)
    }

    fun toggleFlash() {
        camera?.let { camera ->
            if (camera.cameraInfo.hasFlashUnit()) {
                val turnFlashOn = _isFlashOn.value == false

                camera.cameraControl.enableTorch(turnFlashOn)
                _isFlashOn.postValue(turnFlashOn)
            }
        }
    }

    fun updateExposure(value: Float) {
        camera?.let { camera ->
            val range = camera.cameraInfo.exposureState.exposureCompensationRange
            val exposure = ((value * 2f) - 1f) * range.upper
            camera.cameraControl.setExposureCompensationIndex(exposure.toInt())
        }
    }

    fun updateZoom(delta: Float) {
        camera?.let { camera ->
            val currentZoomRatio = camera.cameraInfo.zoomState.value?.zoomRatio ?: 1f
            camera.cameraControl.setZoomRatio(currentZoomRatio * delta)
        }
    }

    fun focus(point: MeteringPoint) {
        val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF or FocusMeteringAction.FLAG_AE)
            .setAutoCancelDuration(5, TimeUnit.SECONDS)
            .build()
        camera?.cameraControl?.startFocusAndMetering(action)
    }

    fun takePhoto(@NonNull context: Context) {
        val file = File(context.filesDir, SaveToMediaStore.getImageFileName())

        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    _didTakePicture.postValue(Uri.fromFile(file))
                }

                override fun onError(exception: ImageCaptureException) {}
            }
        )
    }
}