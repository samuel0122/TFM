package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentBooksListBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.DialogsFactory
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.BooksListAdapter
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.BooksListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksListFragment : Fragment(), MenuProvider {
    private val viewModel: BooksListViewModel by viewModels()

    private lateinit var binding: FragmentBooksListBinding

    private val booksListAdapter = BooksListAdapter()

    private var isEditMode: Boolean = false
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksListBinding.inflate(inflater)

        binding.rcBooks.apply {
            adapter = booksListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(
                BooksListFragmentDirections.actionBooksListFragmentToEditBookDialog()
            )
        }

        booksListAdapter.setOnItemClickListener { book ->
            if (isEditMode) {
                viewModel.selectBook(book.id)
            } else {
                findNavController().navigate(
                    BooksListFragmentDirections.actionBooksListFragmentToPagesListFragment(bookId = book.id)
                )
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.booksModel.observe(viewLifecycleOwner) { booksItems ->
            booksListAdapter.submitList(booksItems)
        }

        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            this.isEditMode = isEditMode
            booksListAdapter.setEditMode(isEditMode)
            updateMenuVisibility()
        }

        viewModel.isAllSelected.observe(viewLifecycleOwner) { isAllSelected ->
            menu?.findItem(R.id.action_select_all)?.apply {
                isChecked = isAllSelected
                setIcon(if (isAllSelected) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked)
            }
        }

        viewModel.selectedBooksIds.observe(viewLifecycleOwner) { selectedBooksIds ->
            booksListAdapter.setSelectedItemsIds(selectedBooksIds)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_books_list, menu)

        this.menu = menu
        updateMenuVisibility()
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_select_all -> {
                viewModel.toggleAllSelection()
                true
            }

            R.id.action_delete_books -> {
                confirmDeleteSelectedBooks()
                true
            }

            R.id.action_disable_edit_mode -> {
                viewModel.disableEditMode()
                true
            }

            R.id.action_enable_edit_mode -> {
                viewModel.enableEditMode()
                true
            }

            else -> false
        }
    }

    private fun confirmDeleteSelectedBooks() {
        DialogsFactory.confirmationDialog(requireContext(),
            title = "Confirm delete selected books",
            question = "Are you sure you want to delete the selected books?",
            onConfirmAction = { viewModel.deleteBooks() },
            onCancelAction = { dialog -> dialog.dismiss() })
    }

    private fun updateMenuVisibility() {
        menu?.setGroupVisible(R.id.group_list_edit_mode, isEditMode)
        menu?.setGroupVisible(R.id.group_books_list, !isEditMode)
    }
}