package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pagesList.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions.animatedFadeIn
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions.animatedFadeOut
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions.animatedScale
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions.fromUriScaleDown
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ItemPageBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageState
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.adapters.SelectableListAdapter
import javax.inject.Inject

class PagesListAdapter @Inject constructor(
    diffCallback: DiffUtil.ItemCallback<PageItem>
) : SelectableListAdapter<PageItem, ItemPageBinding>(diffCallback) {

    override fun createBinding(parent: ViewGroup): ItemPageBinding =
        ItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bindLayout(
        holder: SelectableItemHolder<ItemPageBinding>,
        item: PageItem
    ) {
        holder.itemView.post {
            holder.itemView.layoutParams.height = holder.itemView.width

            holder.binding.apply {
                // root.transitionName = "pageTransition${item.id}"
                cvHolder.radius = holder.itemView.width * 0.05f

                if (item.processState == PageState.Processing) {
                    shimmer.visibility = View.VISIBLE
                    ivPage.visibility = View.GONE
                    shimmer.startShimmer()
                } else {
                    shimmer.stopShimmer()
                    ivPage.visibility = View.VISIBLE
                    shimmer.visibility = View.GONE
                }

                ivPageShimmer.fromUriScaleDown(Uri.parse(item.imageUri), holder.itemView.width)
                ivPage.fromUriScaleDown(item.imageUri.toUri(), holder.itemView.width)
            }
        }
    }

    override fun bindEditMode(binding: ItemPageBinding, isEditMode: Boolean, isSelected: Boolean) {
        binding.vSelectedMark.apply {
            if (isEditMode) {
                setImageResource(if (isSelected) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked)
                animatedFadeIn(150)
            } else {
                animatedFadeOut(150)
            }
        }

        binding.cvHolder.apply {
            if (isSelected) {
                animatedScale(0.9f, 0.9f, 200)
            } else {
                animatedScale(1f, 1f, 200)
            }
        }
    }
}