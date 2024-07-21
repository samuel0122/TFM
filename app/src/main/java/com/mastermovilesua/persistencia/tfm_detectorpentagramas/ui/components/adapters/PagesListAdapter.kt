package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions.fromUriScaleDown
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ItemPageBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import javax.inject.Inject

class PagesListAdapter @Inject constructor() :
    SelectableListAdapter<PageItem, ItemPageBinding>(Companion) {
    companion object : DiffUtil.ItemCallback<PageItem>() {
        override fun areItemsTheSame(oldItem: PageItem, newItem: PageItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PageItem, newItem: PageItem): Boolean =
            oldItem == newItem
    }

    override fun createBinding(parent: ViewGroup): ItemPageBinding =
        ItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bindLayout(holder: SelectableItemHolder<ItemPageBinding>, item: PageItem) {
        holder.itemView.post {
            holder.itemView.layoutParams =
                holder.itemView.layoutParams.also { it.height = holder.itemView.width }

            holder.binding.apply {
                cvHolder.radius = holder.itemView.width * 0.05f
                ivPage.fromUriScaleDown(Uri.parse(item.imageUri), holder.itemView.width)
                vSelectedMark.isVisible = false
            }
        }
    }

    override fun bindEditMode(binding: ItemPageBinding, isEditMode: Boolean, isSelected: Boolean) {
        binding.vSelectedMark.isVisible = isEditMode

        binding.cvHolder.apply {
            scaleX = if (isSelected) 0.9f else 1f
            scaleY = if (isSelected) 0.9f else 1f
        }

        if (isEditMode) {
            binding.vSelectedMark.setImageResource(if (isSelected) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked)
        }
    }
}