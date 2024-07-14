package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.FragmentPageDetailBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.PageDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PageDetailFragment : Fragment() {
    private val viewModel: PageDetailViewModel by viewModels()
    private val args: PageDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentPageDetailBinding

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageDetailBinding.inflate(inflater)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.pageModel.observe(this) {
            binding.ivPage.setImageURI(Uri.parse(it.imageUri))
        }

        viewModel.onCreate(args.pageId)
    }
}