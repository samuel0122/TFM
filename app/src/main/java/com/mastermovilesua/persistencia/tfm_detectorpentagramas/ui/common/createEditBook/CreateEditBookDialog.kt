package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common.createEditBook

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
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.DialogEditBookBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.Dataset
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateEditBookDialog : DialogFragment() {
    private val viewModel: CreateEditBookViewModel by viewModels()
    private val args: CreateEditBookDialogArgs by navArgs()

    private lateinit var binding: DialogEditBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onCreate(args.bookId, args.isEditing)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditBookBinding.inflate(layoutInflater)

        binding.apply {
            if (args.isEditing) {
                tvHeader.text = getString(R.string.edit_book_header)
                btnSubmit.text = getString(R.string.edit_book_button)
            } else {
                tvHeader.text = getString(R.string.create_book_header)
                btnSubmit.text = getString(R.string.create_book_button)
            }
        }

        setupSpinner()
        setupClickListeners()

        observeViewModel()

        return MaterialAlertDialogBuilder(requireActivity())
            .setView(binding.root)
            .create()
            .apply { window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) }
    }


    private fun setupSpinner() {
        binding.spDataset.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                Dataset.entries.map { it.name }).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
    }

    private fun setupClickListeners() {
        binding.btnSubmit.setOnClickListener {
            viewModel.onSubmit(
                title = binding.etTitle.text.toString(),
                description = binding.etDescription.text.toString(),
                dataset = binding.spDataset.selectedItemId.toInt()
            )
        }
    }

    private fun observeViewModel() {
        viewModel.bookModel.observe(this) { book ->
            binding.apply {
                if (args.isEditing) tvHeader.text = getString(R.string.edit_book_parametrized_header, book.title)
                etTitle.setText(book.title)
                etDescription.setText(book.description)
                spDataset.setSelection(book.dataset.value)
            }
        }

        viewModel.titleError.observe(this) { titleError ->
            binding.etTitle.error = titleError
        }

        viewModel.descriptionError.observe(this) { descriptionError ->
            binding.etDescription.error = descriptionError
        }

        viewModel.bookSubmitted.observe(this) { isSubmited ->
            if (isSubmited)
                findNavController().navigateUp()
        }
    }
}