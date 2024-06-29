package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.AppDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.ImagenesCargadasDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ActivityMainBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ActivityViewImageDetailedBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.ImageDetailViewModel
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewImageDetailedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewImageDetailedBinding

    private val viewModel: ImageDetailViewModel by viewModels()

    object EXTRAS {
        const val EXTRA_IMAGE_ID = "EXTRA_IMAGE_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewImageDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageId = intent.getIntExtra(EXTRAS.EXTRA_IMAGE_ID, 0)

        viewModel.imageModel.observe(this) {
            binding.imageViewDetailed.setImageURI(Uri.parse(it.dirImagen))
        }

        viewModel.onCreate(imageId)
    }
}