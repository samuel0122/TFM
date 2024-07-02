package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ItemBookBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.OnItemClickListener

class BooksListAdapter :
    ListAdapter<BookItem, BooksListAdapter.BookItemHolder>(Companion),
    OnItemClickListener<BookItem>
{
    companion object : DiffUtil.ItemCallback<BookItem>() {
        override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean =
            oldItem.bookId == newItem.bookId

        override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean =
            oldItem == newItem
    }

    inner class BookItemHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemHolder {
        return BookItemHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookItemHolder, position: Int) {
        val book = currentList[position]

        holder.binding.tvTitle.text = book.title
        holder.binding.tvDescription.text = book.description

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(book) }
        }
    }

    private var onItemClickListener : ((BookItem) -> Unit)? = null

    override fun setOnItemClickListener(listener: (BookItem) -> Unit) {
        onItemClickListener = listener
    }

}