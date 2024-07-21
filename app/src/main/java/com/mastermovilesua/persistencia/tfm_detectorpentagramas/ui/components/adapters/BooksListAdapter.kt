package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ItemBookBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import javax.inject.Inject

class BooksListAdapter @Inject constructor() :
    SelectableListAdapter<BookItem, ItemBookBinding>(Companion) {

    companion object : DiffUtil.ItemCallback<BookItem>() {
        override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean =
            oldItem == newItem
    }

    override fun createBinding(parent: ViewGroup): ItemBookBinding =
        ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bindLayout(holder: SelectableItemHolder<ItemBookBinding>, item: BookItem) {
        holder.binding.tvTitle.text = item.title
        holder.binding.tvDescription.text = item.description

        holder.binding.vSelectedMark.isVisible = false
    }

    override fun bindEditMode(binding: ItemBookBinding, isEditMode: Boolean, isSelected: Boolean) {
        binding.vSelectedMark.isVisible = isEditMode

        binding.cvBook.apply {
            scaleX = if (isSelected) 0.9f else 1f
            scaleY = if (isSelected) 0.9f else 1f
        }

        if (isEditMode) {
            binding.vSelectedMark.setImageResource(if (isSelected) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked)
        }
    }
}