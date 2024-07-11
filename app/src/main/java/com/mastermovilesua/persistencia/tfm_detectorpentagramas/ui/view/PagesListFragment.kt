package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentPagesListBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.GridSpacingItemDecoration
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components.adapters.PagesGridAdapter
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.PagesListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagesListFragment : Fragment() {
    private val viewModel: PagesListViewModel by viewModels()
    private val args: PagesListFragmentArgs by navArgs()

    private lateinit var binding: FragmentPagesListBinding

    private val pagesGridAdapter = PagesGridAdapter()

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagesListBinding.inflate(inflater)

        binding.rvPagesList.adapter = pagesGridAdapter
        changeImageGridNumColumns()

        binding.addButton.setOnClickListener { _ -> onClickAddAction() }

        pagesGridAdapter.setOnItemClickListener { page ->
            findNavController().navigate(
                PagesListFragmentDirections.actionPagesListFragmentToPageDetailFragment(pageId = page.pageId)
            )
        }

        viewModel.pagesModel.observe(viewLifecycleOwner) { pagesList ->
            pagesGridAdapter.submitList(pagesList)
        }

        viewModel.onCreate(args.bookId)

        return binding.root
    }

    private fun changeImageGridNumColumns() {
        val numColumns = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            5
        } else {
            3
        }

        binding.rvPagesList.layoutManager = GridLayoutManager(context, numColumns)

        // Remove any previous item decorator
        while (binding.rvPagesList.itemDecorationCount > 0) {
            binding.rvPagesList.removeItemDecorationAt(0)
        }

        // Add the new item decoration
        binding.rvPagesList.addItemDecoration(GridSpacingItemDecoration(numColumns, 16, true))
    }

    private fun onClickAddAction() {
        findNavController().navigate(
            PagesListFragmentDirections.actionPagesListFragmentToAddPageDialog(bookId = args.bookId)
        )
    }
}