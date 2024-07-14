package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.components

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.databinding.DialogInputBinding

class QuantityDialog : DialogFragment() {

    private val onSubmitClickListener: (Float) -> Unit = {}

    private lateinit var binding : DialogInputBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogInputBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.bAddQuantity.setOnClickListener {
            onSubmitClickListener(
                binding.etAmount.text.toString().toFloat()
            )
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

}