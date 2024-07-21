package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.editBook

import android.R
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.DialogEditBookBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.Dataset
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditBookDialog : DialogFragment() {
    private val viewModel: EditBookViewModel by viewModels()
    private val args: EditBookDialogArgs by navArgs()

    private lateinit var binding: DialogEditBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate(args.bookId)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditBookBinding.inflate(layoutInflater)

        setupSpinner()
        observeViewModel()
        setupClickListeners()

        return MaterialAlertDialogBuilder(requireActivity())
            .setView(binding.root)
            .create()
            .apply { window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) }
    }
    private fun setupSpinner() {
        binding.spDataset.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item,
                Dataset.entries.map { it.name }).apply {
                setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            }
    }

    private fun observeViewModel() {
        viewModel.bookModel.observe(this) { book ->
            binding.etTitle.setText(book.title)
            binding.etDescription.setText(book.description)
            binding.spDataset.setSelection(book.dataset.value)
        }
    }

    private fun setupClickListeners() {
        binding.bAddQuantity.setOnClickListener {
            viewModel.onSubmit(
                title = binding.etTitle.text.toString(),
                description = binding.etDescription.text.toString(),
                dataset = binding.spDataset.selectedItemId.toInt()
            )
            findNavController().navigateUp()
        }
    }
}