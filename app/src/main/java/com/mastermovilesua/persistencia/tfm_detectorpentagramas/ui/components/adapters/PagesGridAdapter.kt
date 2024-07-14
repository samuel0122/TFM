package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions.fromUriScaleDown
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ItemPageBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.PageItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.OnItemClickListener

class PagesGridAdapter :
    ListAdapter<PageItem, PagesGridAdapter.PageItemHolder>(Companion),
    OnItemClickListener<PageItem>
{
    companion object : DiffUtil.ItemCallback<PageItem>() {
        override fun areItemsTheSame(oldItem: PageItem, newItem: PageItem): Boolean =
            oldItem.pageId == newItem.pageId

        override fun areContentsTheSame(oldItem: PageItem, newItem: PageItem): Boolean =
            oldItem == newItem
    }

    inner class PageItemHolder(val binding: ItemPageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageItemHolder {
        return PageItemHolder(
            ItemPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PageItemHolder, position: Int) {
        val page = currentList[position]

        holder.itemView.post {
            holder.itemView.layoutParams = holder.itemView.layoutParams.also { it.height = holder.itemView.width }

            holder.binding.cvHolder.radius = holder.itemView.width * 0.05f

            holder.binding.ivPage.fromUriScaleDown(Uri.parse(page.imageUri), holder.itemView.width)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(page) }
        }
    }

    private var onItemClickListener : ((PageItem) -> Unit)? = null

    override fun setOnItemClickListener(listener: (PageItem) -> Unit) {
        onItemClickListener = listener
    }
}