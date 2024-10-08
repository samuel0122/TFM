package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.bookDetail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.DialogAddPageBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.bookDetail.viewModel.AddPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPageDialog : BottomSheetDialogFragment() {
    private val viewModel: AddPageViewModel by viewModels()
    private val args: AddPageDialogArgs by navArgs()

    private lateinit var binding: DialogAddPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddPageBinding.inflate(inflater)

        viewModel.onCreate(args.bookId)

        binding.btnOneImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnMultipleImages.setOnClickListener {
            pickMultipleImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnTakePicture.setOnClickListener { takePhoto() }

        binding.btnDownloadPictures.setOnClickListener { viewModel.downloadPictures() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pageInserted.observe(viewLifecycleOwner) { isPagesInserted ->
            if (isPagesInserted) {
                findNavController().navigateUp()
            }
        }
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { viewModel.insertPages(listOf(uri)) }
        }

    private val pickMultipleImage =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            viewModel.insertPages(uris)
        }

    private fun takePhoto() {
        findNavController().navigate(
            AddPageDialogDirections.actionAddPageDialogToCameraFragment(bookId = args.bookId)
        )
    }
}