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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.eCameraState
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.Permissions
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentCameraBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.camera.CameraFragment
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.AddPageCameraViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddPageCameraFragment : CameraFragment() {
    override val viewModel: AddPageCameraViewModel by viewModels()

    private val args: AddPageCameraFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
       val cameraView = super.onCreateView(inflater, container, savedInstanceState)

        binding.btnConfirmPhoto.setOnClickListener {
            viewModel.insertCapturedPage()
            findNavController().navigateUp()
        }

        viewModel.onCreate(args.bookId)

        return cameraView
    }
}