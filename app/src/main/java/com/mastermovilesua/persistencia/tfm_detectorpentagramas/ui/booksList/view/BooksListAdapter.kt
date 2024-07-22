package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.booksList.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions.animatedFadeIn
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions.animatedFadeOut
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions.animatedScale
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
        item: BookItem
    ) {
        holder.binding.apply {
            tvTitle.text = item.title
            tvDescription.text = item.description
        }
    }

    override fun bindEditMode(binding: ItemBookBinding, isEditMode: Boolean, isSelected: Boolean) {
        binding.cvBook.apply {
            if (isSelected) {
                animatedScale(0.9f, 0.9f, 200)
            } else {
                animatedScale(1f, 1f, 200)
            }
        }

        binding.vSelectedMark.apply {
            if (isEditMode) {
                setImageResource(if (isSelected) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked)
                animatedFadeIn(150)
            } else {
                animatedFadeOut(150)
            }
        }
    }
}