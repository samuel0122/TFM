package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.ActivityMainBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentBooksListBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.BooksListAdapter
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.ImagenesRecyclerGridAdapter
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.OnGridItemClickAction
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.BooksListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksListFragment : Fragment() {
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
}