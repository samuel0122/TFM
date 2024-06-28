package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.adapters

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
) : ListAdapter<ImagenesItem, ImagenesRecyclerGridAdapter.ImagenViewHolder>(ImagenDiffCallback()) {

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

class ImagenDiffCallback : DiffUtil.ItemCallback<ImagenesItem>() {
    override fun areItemsTheSame(oldItem: ImagenesItem, newItem: ImagenesItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImagenesItem, newItem: ImagenesItem): Boolean {
        return oldItem == newItem
    }
}

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // posición del item
        val column = position % spanCount // columna del item

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}
