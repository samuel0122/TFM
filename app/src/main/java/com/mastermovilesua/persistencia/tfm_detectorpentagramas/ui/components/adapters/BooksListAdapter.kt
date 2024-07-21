package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ItemBookBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.adapters.SelectableListAdapter
import javax.inject.Inject

class BooksListAdapter @Inject constructor(
    diffCallback: DiffUtil.ItemCallback<BookItem>
) : SelectableListAdapter<BookItem, ItemBookBinding>(diffCallback) {

    override fun createBinding(parent: ViewGroup): ItemBookBinding =
        ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bindLayout(
        holder: SelectableItemHolder<ItemBookBinding>,
        item: BookItem,
        isEditMode: Boolean
    ) {
        holder.binding.apply {
            tvTitle.text = item.title
            tvDescription.text = item.description
            vSelectedMark.isVisible = isEditMode
        }
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