package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters

import android.graphics.Rect
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ImageItemBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ImagenesItem

class ImagenesRecyclerGridAdapter (
    private val onGridItemClickAction: OnGridItemClickAction
) : ListAdapter<ImagenesItem, ImagenesRecyclerGridAdapter.ImagenViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<ImagenesItem>() {
        override fun areItemsTheSame(oldItem: ImagenesItem, newItem: ImagenesItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImagenesItem, newItem: ImagenesItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagenViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagenViewHolder(binding, onGridItemClickAction)
    }

    override fun onBindViewHolder(holder: ImagenViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ImagenViewHolder (
        private val binding: ImageItemBinding,
        private val onGridItemClickAction: OnGridItemClickAction
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(imagen: ImagenesItem) {

            binding.imageViewItem.setImageURI(Uri.parse(imagen.dirImagen))

            binding.root.post {
                val layoutParams = binding.root.layoutParams
                layoutParams.height = binding.root.width
                binding.root.layoutParams = layoutParams
            }

            binding.imageViewItem.setOnClickListener {
                onGridItemClickAction.onGridItemClickAction(imagen.id)
            }
        }
    }
}
