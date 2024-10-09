package com.mastermovilesua.persistencia.tfm_detectorpentagramas.ui.common

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mastermovilesua.persistencia.tfm_detectorpentagramas.R

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
            .setPositiveButton(context.getText(R.string.confirm)) { dialog, _ -> onConfirmAction.onAction(dialog) }
            .setNegativeButton(context.getText(R.string.cancel)) { dialog, _ -> onCancelAction.onAction(dialog) }
            .show()
    }
}