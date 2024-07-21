package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun interface OnDialogActionListener {
    fun onAction(dialog: DialogInterface)
}

object DialogsFactory {
    fun confirmationDialog(
        context: Context,
        title: String,
        question: String,
        onConfirmAction: OnDialogActionListener,
        onCancelAction: OnDialogActionListener
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(question)
            .setPositiveButton("Confirm") { dialog, _ -> onConfirmAction.onAction(dialog) }
            .setNegativeButton("Cancel") { dialog, _ -> onCancelAction.onAction(dialog) }
            .show()
    }
}