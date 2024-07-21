package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pagesList.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentPagesListBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.components.GridSpacingItemDecoration
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.components.adapters.PagesListAdapter
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.pagesList.viewModel.PagesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PagesListFragment : Fragment(), MenuProvider {
    private val viewModel: PagesListViewModel by viewModels()
    private val args: PagesListFragmentArgs by navArgs()

    private lateinit var binding: FragmentPagesListBinding

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
        binding = FragmentPagesListBinding.inflate(inflater)

        binding.rvPagesList.apply {
            adapter = pagesGridAdapter
        }

        binding.addButton.setOnClickListener { _ ->
            findNavController().navigate(
                PagesListFragmentDirections.actionPagesListFragmentToAddPageDialog(bookId = args.bookId)
            )
        }

        pagesGridAdapter.setOnItemClickListener { page, view ->
            if (isEditMode) {
                viewModel.selectPage(page.id)
            } else {
                findNavController().navigate(
                    PagesListFragmentDirections.actionPagesListFragmentToPageDetailFragment(pageId = page.id),
                    FragmentNavigatorExtras(
                        view to view.transitionName
                    )
                )
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        changeImageGridNumColumns()

        viewModel.bookModel.observe(viewLifecycleOwner) { bookModel ->
            binding.tvTitle.text = bookModel.title
            binding.tvDataset.text = bookModel.dataset.name
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
                    PagesListFragmentDirections.actionPagesListFragmentToEditBookDialog(args.bookId)
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
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm delete selected pages")
            .setMessage("Are you sure you want to delete the selected pages?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deletePages()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun confirmDeleteSelectedBook() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm delete current book")
            .setMessage("Are you sure you want to delete current book (${viewModel.bookModel.value?.title})?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteBook()
                findNavController().navigateUp()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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