package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.Manifest
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.eCameraState
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.Permissions
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentCameraBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.AddPageCameraViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPageCameraFragment : Fragment() {
    private val viewModel: AddPageCameraViewModel by viewModels()
    private val args: AddPageCameraFragmentArgs by navArgs()

    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "On CREATE VIEW")

        binding = FragmentCameraBinding.inflate(inflater)

        binding.btnCloseCamera.setOnClickListener { findNavController().navigateUp() }
        binding.btnFlipCamera.setOnClickListener { viewModel.flipCamera() }
        binding.btnToggleFlash.setOnClickListener { viewModel.toggleFlash() }
        binding.btnShotPhoto.setOnClickListener { viewModel.takePhoto(requireContext()) }

        binding.btnConfirmPhoto.setOnClickListener {
            viewModel.insertCapturedPage()
            findNavController().navigateUp()
        }

        binding.btnDiscardPhoto.setOnClickListener { viewModel.discardCapturedPage() }

        binding.clLive.visibility = View.GONE
        binding.clPreview.visibility = View.GONE

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "On CREATE")

        viewModel.onCreate(args.bookId)

        if (Permissions.hasCameraPermission(requireContext())) {
            startCamera()
        } else {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    startCamera()
                } else {
                    // Manejar el caso en el que no se otorgó el permiso
                    Toast.makeText(
                        requireContext(),
                        "Permission denied. Camera cannot be accessed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }
            }.launch(Permissions.useCameraPerm)
        }

        viewModel.cameraFacing.observe(this) { _ ->
            bindCameraUserCases()
        }

        viewModel.flashOn.observe(this) { isFlashOn ->
            if (isFlashOn) binding.btnToggleFlash.setImageResource(R.drawable.ic_flash_on)
            else binding.btnToggleFlash.setImageResource(R.drawable.ic_flash_off)
        }

        viewModel.pictureUri.observe(this) { savedUri ->
            binding.ivPage.setImageURI(savedUri)
        }

        viewModel.cameraState.observe(this) { cameraState ->
            when (cameraState) {
                eCameraState.Live -> {
                    binding.clLive.visibility = View.VISIBLE
                    binding.clPreview.visibility = View.GONE
                }

                eCameraState.ImageCaptured -> {
                    binding.clLive.visibility = View.GONE
                    binding.clPreview.visibility = View.VISIBLE
                }
            }
        }

        viewModel.isCapturingImage.observe(this) { isCapturingImage ->
            if (isCapturingImage) binding.btnShotPhoto.imageTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_gray)
            else binding.btnShotPhoto.imageTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            viewModel.setCameraProvider(cameraProviderFuture.get())

            viewModel.onStartCamera(binding.pvCamera.display.rotation)

            viewModel.preview.apply {
                setSurfaceProvider(binding.pvCamera.surfaceProvider)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUserCases() {
        try {
            viewModel.cameraProvider?.let { cameraProvider ->
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                val camera = cameraProvider.bindToLifecycle(
                    this,
                    viewModel.cameraSelector,
                    viewModel.preview,
                    viewModel.imageCapture
                )

                viewModel.setCamera(camera)

                setUpZoomTapToFocusAndExposure()
            }

        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun setUpZoomTapToFocusAndExposure() {
        // Configuración del control de exposición
        binding.sbExposure.setProgress(50, true)
        viewModel.updateExposure(binding.sbExposure.progress.toFloat() / binding.sbExposure.max.toFloat())
        binding.sbExposure.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBar?.let {
                    viewModel.updateExposure(progress.toFloat() / seekBar.max.toFloat())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Configuración del detector de gestos para el zoom
        val scaleGestureDetector = ScaleGestureDetector(
            requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    viewModel.updateZoom(detector.scaleFactor)
                    return true
                }
            })

        // Configuración del detector de gestos para el enfoque
        val gestureDetector =
            GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    // Mostrar círculo de enfoque
                    binding.focusCircleView.setRectPoint(e.x, e.y)
                    binding.focusCircleView.invalidate()

                    // Focus
                    val factory = binding.pvCamera.meteringPointFactory
                    val point = factory.createPoint(e.x, e.y)
                    viewModel.focus(point)

                    return true
                }
            })

        // Configuración del enfoque táctil y el zoom
        binding.pvCamera.setOnTouchListener { view, event ->
            scaleGestureDetector.onTouchEvent(event)
            if (!scaleGestureDetector.isInProgress) {
                gestureDetector.onTouchEvent(event)
            }
            view.performClick()
            true
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "On DESTROY")
        viewModel.onDestroy()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "AddPageCameraFragment"
    }
}