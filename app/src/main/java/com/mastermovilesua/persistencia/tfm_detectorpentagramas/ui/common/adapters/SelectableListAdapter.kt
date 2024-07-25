package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.ID
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.IdentifiableItem

abstract class SelectableListAdapter<T : IdentifiableItem, VB : ViewBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, SelectableListAdapter<T, VB>.SelectableItemHolder<VB>>(diffCallback),
    OnItemClickListenerConsumer<T> {

    inner class SelectableItemHolder<VB : ViewBinding>(val binding: VB) :
        RecyclerView.ViewHolder(binding.root)

    // Creating binding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableItemHolder<VB> {
        return SelectableItemHolder(createBinding(parent))
    }

    abstract fun createBinding(parent: ViewGroup): VB

    // Binding view
    override fun onBindViewHolder(holder: SelectableItemHolder<VB>, position: Int) {
        val item = currentList[position]
        val isSelected = selectedItemsIds.contains(item.id)

        holder.itemView.setOnClickListener { _ ->
            onItemClickListener?.onItemClick(item, holder.binding.root)
        }

        Log.e(
            "SelectableListAdapter",
            "Update all interface of index [$position] isEditMode [$isEditMode] and selected [$isSelected]"
        )
        bindLayout(holder, item)
        bindEditMode(holder.binding, isEditMode, isSelected)
    }

    override fun onBindViewHolder(
        holder: SelectableItemHolder<VB>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val item = currentList[position]
            val isSelected = selectedItemsIds.contains(item.id)
            Log.e(
                "SelectableListAdapter",
                "Update edit mode of index [$position] isEditMode [$isEditMode] and selected [$isSelected]"
            )

            bindEditMode(holder.binding, isEditMode, isSelected)
        }
    }

    abstract fun bindLayout(holder: SelectableItemHolder<VB>, item: T)

    abstract fun bindEditMode(binding: VB, isEditMode: Boolean, isSelected: Boolean)

    /// Item selection
    private var onItemClickListener: OnItemClickListener<T>? = null

    override fun setOnItemClickListener(listener: OnItemClickListener<T>) {
        onItemClickListener = listener
    }

    // Edit Mode
    private var isEditMode = false

    fun setEditMode(editMode: Boolean) {
        isEditMode = editMode
        notifyItemRangeChanged(0, itemCount, isEditMode)
    }

    // Items selected
    private var selectedItemsIds: HashSet<ID> = HashSet()

    fun setSelectedItemsIds(newSelectedPagesIds: Set<ID>) {
        val previouslySelectedItemsIds = selectedItemsIds
        selectedItemsIds = newSelectedPagesIds.toHashSet()

        val nonChangedElements: Set<ID> = newSelectedPagesIds.intersect(previouslySelectedItemsIds)
        val newElements: Set<ID> = newSelectedPagesIds.minus(nonChangedElements)
        val deletedElements: Set<ID> = previouslySelectedItemsIds.minus(nonChangedElements)
        val changedElements: HashSet<ID> = newElements.plus(deletedElements).toHashSet()

        Log.e(
            "SelectableListAdapter",
            "Number of new [${newElements.size}] and of deleted: [${deletedElements.size}] for total of: [${changedElements.size}] -> [$changedElements]"
        )

        currentList.forEachIndexed { index, item ->
            if (changedElements.contains(item.id)) {
                notifyItemChanged(index, isEditMode)
            }
        }
    }
}