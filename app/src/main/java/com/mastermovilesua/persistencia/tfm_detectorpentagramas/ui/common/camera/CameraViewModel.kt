package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.camera

import android.content.Context
import android.net.Uri
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
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

enum class CameraState {
    Live,
    ImageCaptured
}

enum class CameraFacing {
    Front,
    Back
}

open class CameraViewModel @Inject constructor() : ViewModel() {
    private val _flashOn = MutableLiveData<Boolean>()
    private val _isCapturingImage = MutableLiveData<Boolean>()
    private val _cameraFacing = MutableLiveData<CameraFacing>()
    private val _pictureUri = MutableLiveData<Uri>()
    private val _cameraState = MutableLiveData<CameraState>()

    val flashOn: LiveData<Boolean> get() = _flashOn
    val isCapturingImage: LiveData<Boolean> get() = _isCapturingImage
    val cameraFacing: LiveData<CameraFacing> get() = _cameraFacing
    val pictureUri: LiveData<Uri> get() = _pictureUri
    val cameraState: LiveData<CameraState> get() = _cameraState

    private var _cameraProvider: ProcessCameraProvider? = null
    private var _camera: Camera? = null

    val camera: Camera? get() = _camera
    val cameraProvider: ProcessCameraProvider? get() = _cameraProvider

    lateinit var preview: Preview
    lateinit var imageCapture: ImageCapture
    lateinit var cameraSelector: CameraSelector

    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var aspectRatio = AspectRatio.RATIO_16_9

    private lateinit var cameraExecutor: ExecutorService

    fun onCreate() {
        cameraExecutor = Executors.newSingleThreadExecutor()

        pictureUri.value?.let { pictureUriValue -> _pictureUri.postValue(pictureUriValue) }

        buildCameraSelector()
    }

    fun onStartCamera(rotation: Int) {
        viewModelScope.launch {
            buildImageCapture(rotation)

            buildPreview(rotation)

            _cameraState.postValue(cameraState.value ?: CameraState.Live)
            _flashOn.postValue(flashOn.value ?: false)
            _isCapturingImage.postValue(isCapturingImage.value ?: false)
            _cameraFacing.postValue(cameraFacing.value ?: CameraFacing.Back)
        }
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
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setResolutionSelector(resolutionSelector)
            .setTargetRotation(rotation)
            .build()
    }

    private fun buildCameraSelector() {
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
    }

    fun discardCapturedPage(context: Context) {
        _pictureUri.value?.let { pictureUri ->
            SaveToMediaStore.deleteImage(context, pictureUri)
        }
        _cameraState.postValue(CameraState.Live)
    }

    fun setCameraProvider(cameraProvider: ProcessCameraProvider) {
        _cameraProvider = cameraProvider
    }

    fun setCamera(camera: Camera) {
        _camera = camera
    }

    fun flipCamera() {
        cameraFacing.value?.let { cameraFacing ->
            viewModelScope.launch {
                when (cameraFacing) {
                    CameraFacing.Front -> {
                        lensFacing = CameraSelector.LENS_FACING_BACK
                        _cameraFacing.postValue(CameraFacing.Back)
                    }

                    CameraFacing.Back -> {
                        lensFacing = CameraSelector.LENS_FACING_FRONT
                        _cameraFacing.postValue(CameraFacing.Front)
                    }
                }

                buildCameraSelector()
            }
        }
    }

    fun toggleFlash() {
        viewModelScope.launch {
            camera?.let { camera ->
                if (camera.cameraInfo.hasFlashUnit()) {
                    val turnFlashOn = _flashOn.value == false

                    camera.cameraControl.enableTorch(turnFlashOn)
                    _flashOn.postValue(turnFlashOn)
                }
            }
        }
    }

    fun updateExposure(value: Float) {
        viewModelScope.launch {
            camera?.let { camera ->
                val range = camera.cameraInfo.exposureState.exposureCompensationRange
                val exposure = ((value * 2f) - 1f) * range.upper
                camera.cameraControl.setExposureCompensationIndex(exposure.toInt())
            }
        }
    }

    fun updateZoom(delta: Float) {
        viewModelScope.launch {
            camera?.let { camera ->
                val currentZoomRatio = camera.cameraInfo.zoomState.value?.zoomRatio ?: 1f
                camera.cameraControl.setZoomRatio(currentZoomRatio * delta)
            }
        }
    }

    fun focus(point: MeteringPoint) {
        viewModelScope.launch {
            val action = FocusMeteringAction.Builder(
                point,
                FocusMeteringAction.FLAG_AF or FocusMeteringAction.FLAG_AE
            )
                .setAutoCancelDuration(5, TimeUnit.SECONDS)
                .build()
            camera?.cameraControl?.startFocusAndMetering(action)
        }
    }

    fun takePhoto(context: Context) {
        viewModelScope.launch {
            _isCapturingImage.postValue(true)

            val file = SaveToMediaStore.getFileForImageFile(context, SaveToMediaStore.getImageFileName())

            val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            imageCapture.takePicture(
                outputOptions,
                cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        _pictureUri.postValue(SaveToMediaStore.getImageUriForImageFile(context, file))
                        _cameraState.postValue(CameraState.ImageCaptured)
                        _isCapturingImage.postValue(false)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        _isCapturingImage.postValue(false)
                    }
                }
            )
        }
    }
}