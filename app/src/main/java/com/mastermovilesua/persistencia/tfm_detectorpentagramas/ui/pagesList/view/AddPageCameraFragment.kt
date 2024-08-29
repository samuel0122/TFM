package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pagesList.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.camera.CameraFragment
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pagesList.viewModel.AddPageCameraViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPageCameraFragment : CameraFragment() {
    override val viewModel: AddPageCameraViewModel by viewModels()

    private val args: AddPageCameraFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState).also { viewModel.onCreate(args.bookId) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.didInsertPage.observe(viewLifecycleOwner) { didInsertPage ->
            if (didInsertPage) findNavController().navigateUp()
        }
    }

    override fun onImageConfirmationAction() {
        viewModel.insertCapturedPage()
    }
}