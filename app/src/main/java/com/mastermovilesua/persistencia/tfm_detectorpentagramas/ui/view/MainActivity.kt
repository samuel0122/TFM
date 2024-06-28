package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.entities.ImagenesCargadas
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.utils.SaveToMediaStore
import android.content.res.Configuration;
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.add
import androidx.recyclerview.widget.GridLayoutManager
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.adapters.GridSpacingItemDecoration
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.adapters.ImagenesRecyclerGridAdapter
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.adapters.OnGridItemClickAction
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ActivityMainBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.toDomain
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnGridItemClickAction {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private lateinit var recyclerGridAdapter: ImagenesRecyclerGridAdapter

    private lateinit var booksListFragment: ImagenesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        booksListFragment = ImagenesFragment()

        initImagesView()
        initListeners()

        changeImageGridNumColumns()

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(binding.mainFragment.id, booksListFragment)
        }

        viewModel.imagenesModel.observe(this) {
            recyclerGridAdapter.submitList(it)
        }

        viewModel.onCreate()
    }

    private fun initImagesView() {
        recyclerGridAdapter = ImagenesRecyclerGridAdapter(this)

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

        binding.gridImages.layoutManager = GridLayoutManager(this, numColumns)

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
    }

    private fun launchImagePicker() {
        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val imageCopyUri = SaveToMediaStore.saveImageToInternalStorage(this, uri)
                insertImageIntoDatabase(imageCopyUri)
            }
        }

    private val pickMultipleImage =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris != null) {
                for (uri in uris) {
                    val imageCopyUri = SaveToMediaStore.saveImageToInternalStorage(this, uri)
                    insertImageIntoDatabase(imageCopyUri)
                }
            }
        }

    override fun onGridItemClickAction(gridItemId: Int) {
        navigateToViewImage(gridItemId)
    }

    private fun navigateToViewImage(imageId: Int) {
        startActivity(
            Intent(this@MainActivity, ViewImageDetailedActivity::class.java).apply {
                putExtra(ViewImageDetailedActivity.EXTRAS.EXTRA_IMAGE_ID, imageId)
            }
        )
    }
}