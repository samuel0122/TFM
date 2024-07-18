package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentBooksListBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.BooksListAdapter
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.BooksListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksListFragment : Fragment(), MenuProvider {
    private val viewModel: BooksListViewModel by viewModels()

    private lateinit var binding: FragmentBooksListBinding

    private val booksListAdapter = BooksListAdapter()

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksListBinding.inflate(inflater)

        binding.rcBooks.apply {
            adapter = booksListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        booksListAdapter.setOnItemClickListener { book ->
            findNavController().navigate(
                BooksListFragmentDirections.actionBooksListFragmentToPagesListFragment(bookId = book.bookId)
            )
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(
                BooksListFragmentDirections.actionBooksListFragmentToEditBookDialog()
            )
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.booksModel.observe(this) { booksItems ->
            booksListAdapter.submitList(booksItems)
        }

        viewModel.onCreate()
    }

    private fun changeImageGridNumColumns() {
        val numColumns = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            5
        } else {
            3
        }

        binding.rcBooks.layoutManager = GridLayoutManager(context, numColumns)

        // Remove any previous item decorator
        while (binding.rcBooks.itemDecorationCount > 0) {
            binding.rcBooks.removeItemDecorationAt(0)
        }

        // Add the new item decoration
        // binding.rcBooks.addItemDecoration(GridSpacingItemDecoration(numColumns, 16, true))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_books_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_delete_books -> {
                true
            }
            else -> false
        }
    }
}