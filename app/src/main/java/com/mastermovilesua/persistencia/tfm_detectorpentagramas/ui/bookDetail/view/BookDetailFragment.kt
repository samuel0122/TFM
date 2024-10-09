package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.bookDetail.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentBookDetailBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.bookDetail.viewModel.BookDetailViewModel
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.DialogsFactory
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.components.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookDetailFragment : Fragment(), MenuProvider {
    private val viewModel: BookDetailViewModel by viewModels()
    private val args: BookDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentBookDetailBinding

    @Inject
    lateinit var pagesGridAdapter: PagesListAdapter

    private var isEditMode: Boolean = false
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onCreate(args.bookId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookDetailBinding.inflate(inflater)

        changeImageGridNumColumns()
        binding.rvPagesList.apply {
            isNestedScrollingEnabled = false
            adapter = pagesGridAdapter
            setHasFixedSize(true)
        }
        itemTouchHelper.attachToRecyclerView(binding.rvPagesList)

        binding.addButton.setOnClickListener { _ ->
            findNavController().navigate(
                BookDetailFragmentDirections.actionPagesListFragmentToAddPageDialog(bookId = args.bookId)
            )
        }

        pagesGridAdapter.setOnItemClickListener { page, _ ->
            if (isEditMode) {
                viewModel.selectPage(page.id)
            } else {
                findNavController().navigate(
                    BookDetailFragmentDirections.actionPagesListFragmentToPagesPagerFragment(
                        pageId = page.id,
                        bookId = viewModel.bookModel.value!!.id,
                        title = viewModel.bookModel.value?.title.orEmpty()
                    )
                    // , FragmentNavigatorExtras(
                    //     view to view.transitionName
                    // )
                )
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = args.title

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.bookModel.observe(viewLifecycleOwner) { bookModel ->
            binding.apply {
                (activity as AppCompatActivity).supportActionBar?.title = bookModel.title
                // tvTitle.text = bookModel.title
                tvDescription.text = bookModel.description
                tvDataset.text = bookModel.dataset.name
            }
        }

        viewModel.pagesModel.observe(viewLifecycleOwner) { pagesList ->
            pagesGridAdapter.submitList(pagesList)
        }

        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            this.isEditMode = isEditMode
            pagesGridAdapter.setEditMode(isEditMode)
            updateMenuVisibility()
        }

        viewModel.isAllSelected.observe(viewLifecycleOwner) { isAllSelected ->
            menu?.findItem(R.id.action_select_all)?.apply {
                isChecked = isAllSelected
                setIcon(if (isAllSelected) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked)
            }
        }

        viewModel.selectedPagesIds.observe(viewLifecycleOwner) { selectedPagesIds ->
            pagesGridAdapter.setSelectedItemsIds(selectedPagesIds)
        }

        viewModel.bookDeleted.observe(viewLifecycleOwner) { isBookDeleted ->
            if (isBookDeleted) findNavController().navigateUp()
        }
    }

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallBack =
            object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val adapter = recyclerView.adapter as PagesListAdapter
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition

                    viewModel.changePageOrder(adapter.currentList[from].id, to)

                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

                override fun onSelectedChanged(
                    viewHolder: RecyclerView.ViewHolder?,
                    actionState: Int
                ) {
                    super.onSelectedChanged(viewHolder, actionState)
                    if (actionState == ACTION_STATE_DRAG) {
                        viewHolder?.itemView?.alpha = 0.8f
                    }
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)

                    viewHolder.itemView.alpha = 1f
                }
            }

        ItemTouchHelper(simpleItemTouchCallBack)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_pages_list, menu)

        this.menu = menu
        updateMenuVisibility()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_select_all -> {
                viewModel.toggleAllSelection()
                true
            }

            R.id.action_delete_pages -> {
                confirmDeleteSelectedPages()
                true
            }

            R.id.action_disable_edit_mode -> {
                viewModel.disableEditMode()
                true
            }

            R.id.action_edit_book -> {
                findNavController().navigate(
                    BookDetailFragmentDirections.actionPagesListFragmentToEditBookDialog(
                        bookId = args.bookId,
                        isEditing = true
                    )
                )
                true
            }

            R.id.action_delete_book -> {
                confirmDeleteSelectedBook()
                true
            }

            R.id.action_enable_edit_mode -> {
                viewModel.enableEditMode()
                true
            }

            else -> false
        }
    }

    private fun confirmDeleteSelectedPages() {
        DialogsFactory.confirmationDialog(
            context = requireContext(),
            title = getString(R.string.confirm_delete_selected_pages_title),
            question = getString(R.string.confirm_delete_selected_pages),
            onConfirmAction = { viewModel.deletePages() },
            onCancelAction = { dialog -> dialog.dismiss() }
        )
    }

    private fun confirmDeleteSelectedBook() {
        DialogsFactory.confirmationDialog(
            context = requireContext(),
            title = getString(R.string.confirm_delete_current_book_title),
            question = getString(
                R.string.confirm_delete_current_book,
                viewModel.bookModel.value?.title
            ),
            onConfirmAction = { viewModel.deleteBook() },
            onCancelAction = { dialog -> dialog.dismiss() }
        )
    }


    private fun updateMenuVisibility() {
        menu?.setGroupVisible(R.id.group_list_edit_mode, isEditMode)
        menu?.setGroupVisible(R.id.group_pages_list, !isEditMode)
    }

    private fun changeImageGridNumColumns() {
        val numColumns =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 5 else 3

        binding.rvPagesList.apply {
            layoutManager = GridLayoutManager(context, numColumns)

            while (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }

            addItemDecoration(GridSpacingItemDecoration(numColumns, 12, true))
        }
    }
}