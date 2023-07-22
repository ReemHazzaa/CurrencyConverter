package com.reem.currencyconverter.app.extensions

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInVisible() {
    this.visibility = View.INVISIBLE
}

fun Context.showGeneralDialog(
    title: String?,
    description: String?,
    positiveLabel: String? = "Ok",
    negativeLabel: String? = "Cancel",
    cancelable: Boolean = true,
    onClickListener: DialogInterface.OnClickListener?
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(description)
        .setPositiveButton(positiveLabel, onClickListener)
        .setNegativeButton(
            negativeLabel
        ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
        .setCancelable(cancelable)
        .show()
}