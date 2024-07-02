package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.ImagenesRecyclerGridAdapter
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.OnGridItemClickAction
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.ImagenesCargadas
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ActivityMainBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentImagenesBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.toDomain
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.GridSpacingItemDecoration
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.QuantityDialog
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ClassCastException

@AndroidEntryPoint
class ImagenesFragment: Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentImagenesBinding

    private lateinit var onGridItemClickAction: OnGridItemClickAction

    private lateinit var recyclerGridAdapter: ImagenesRecyclerGridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onGridItemClickAction = try {
            context as OnGridItemClickAction
        } catch (e: ClassCastException) {
            throw ClassCastException("$context debe implementar OnItemSelectedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentImagenesBinding.inflate(layoutInflater)

        initImagesView()
        initListeners()

        changeImageGridNumColumns()

        viewModel.imagenesModel.observe(this) {
            recyclerGridAdapter.submitList(it)
        }

        viewModel.onCreate()
    }

    private fun initImagesView() {
        recyclerGridAdapter = ImagenesRecyclerGridAdapter(onGridItemClickAction)

        binding.gridImages.adapter = recyclerGridAdapter
    }

    private fun initListeners() {
        binding.addButton.setOnClickListener { _ -> onClickAddAction() }
    }

    private fun changeImageGridNumColumns() {
        val numColumns = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            5
        } else {
            3
        }

        binding.gridImages.layoutManager = GridLayoutManager(context, numColumns)

        // Remove any previous item decorator
        while (binding.gridImages.itemDecorationCount > 0) {
            binding.gridImages.removeItemDecorationAt(0)
        }

        // Add the new item decoration
        binding.gridImages.addItemDecoration(GridSpacingItemDecoration(numColumns, 16, true))
    }

    private fun insertImageIntoDatabase(selectedImageUri: Uri?) {
        if (selectedImageUri != null) {
            val imagenCargada = ImagenesCargadas(dirImagen = selectedImageUri.toString())

            viewModel.insertImagen(imagenCargada.toDomain())
        }
    }

    private fun onClickAddAction() {
        launchImagePicker()
        /*
        QuantityDialog { quantity ->
            Toast.makeText(requireContext(), "Usted ingreso: $quantity", Toast.LENGTH_SHORT).show()
        }
        .show(parentFragmentManager, "dialog")
         */
    }

    private fun launchImagePicker() {
        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val imageCopyUri = SaveToMediaStore.saveImageToInternalStorage(requireContext(), uri)
                insertImageIntoDatabase(imageCopyUri)
            }
        }

    private val pickMultipleImage =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris != null) {
                for (uri in uris) {
                    val imageCopyUri = SaveToMediaStore.saveImageToInternalStorage(requireContext(), uri)
                    insertImageIntoDatabase(imageCopyUri)
                }
            }
        }

}