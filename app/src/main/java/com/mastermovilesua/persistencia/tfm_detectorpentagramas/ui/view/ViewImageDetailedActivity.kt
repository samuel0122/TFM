package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.AppDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.ImagenesCargadasDao

class ViewImageDetailedActivity : AppCompatActivity() {

    object EXTRAS {
        const val EXTRA_IMAGE_ID = "EXTRA_IMAGE_ID"
    }

    private lateinit var appDatabase: AppDatabase
    private lateinit var imagenesCargadasDao: ImagenesCargadasDao

    private lateinit var imageViewDetailed: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image_detailed)

        initComponents()

        initUI()
    }

    private fun initComponents() {
        appDatabase = AppDatabase.getInstance(this)
        imagenesCargadasDao = appDatabase.imagenesCargadasDao()

        imageViewDetailed = findViewById(R.id.imageViewDetailed)
    }

    private fun initUI() {
        val imageId = intent.getIntExtra(EXTRAS.EXTRA_IMAGE_ID, 0)

        //imagenesCargadasDao.getImageAtPosition(imageId)?.let {
        //    imageViewDetailed.setImageURI(Uri.parse(it.dirImagen))
        //}
    }

}