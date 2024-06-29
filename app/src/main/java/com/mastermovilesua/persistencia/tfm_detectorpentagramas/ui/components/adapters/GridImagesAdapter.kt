package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.AppDatabase
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.data.database.dao.ImagenesCargadasDao
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ImageItemBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ImagenesItem

class GridImagesAdapter (
    private val context: Context,
    val items: List<ImagenesItem>
) : BaseAdapter() {

    private lateinit var binding: ImageItemBinding

    private var itemClickCallback: OnGridItemClickAction = context as OnGridItemClickAction

    private var imageItemWidth = -1

   override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(context), parent, false)

        val view: View = convertView
            ?: LayoutInflater.from(context)
                .inflate(R.layout.image_item, parent, false)


        // val imageView: ImageView = view.findViewById(R.id.imageViewItem)

        val imagenCargada = items[position]

        val imageUriString = imagenCargada.dirImagen

        binding.imageViewItem.setImageURI(Uri.parse(imageUriString))

        if (imageItemWidth == -1)
            calculateImageItemWidth(parent as GridView)

        binding.imageViewItem.layoutParams = ViewGroup.LayoutParams(imageItemWidth, imageItemWidth)

        binding.root.setOnClickListener{ _ -> itemClickCallback.onGridItemClickAction(imagenCargada.id) }

        return binding.root
    }

    private fun calculateImageItemWidth(gridImages: GridView) {
        val numColumns = gridImages.numColumns

        val totalSpacingSpace = (gridImages.horizontalSpacing * (numColumns - 1)) + gridImages.marginLeft + gridImages.marginRight

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val totalAvailableSpace = (screenWidth - totalSpacingSpace)

        imageItemWidth = totalAvailableSpace / numColumns
    }

    fun setNumColumnsChanged() {
        imageItemWidth = -1
    }
}