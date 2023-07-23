package com.reem.currencyconverter.app.extensions

import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
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

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit): TextWatcher {
    val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            var s = editable.toString()
            if (s.isBlank()) s = "1"
            afterTextChanged.invoke(s)
        }
    }

    this.addTextChangedListener(watcher)
    return watcher
}