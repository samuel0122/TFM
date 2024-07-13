package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.camera.CameraFragment
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.AddPageCameraViewModel
import dagger.hilt.android.AndroidEntryPoint

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