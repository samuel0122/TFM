package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.DialogEditBookBinding
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.domain.model.BookItem
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.viewModel.EditBookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditBookDialog : DialogFragment() {
    private val viewModel: EditBookViewModel by viewModels()
    private val args: EditBookDialogArgs by navArgs()

    private lateinit var binding : DialogEditBookBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditBookBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.bAddQuantity.setOnClickListener {
            viewModel.onSubmit(
                title = binding.etTitle.text.toString(),
                description = binding.etDescription.text.toString()
            )
            dismiss()
        }

        viewModel.onCreate(args.bookId)

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

}