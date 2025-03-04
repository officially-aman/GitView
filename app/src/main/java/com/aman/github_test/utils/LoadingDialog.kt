package com.aman.github_test.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.aman.github_test.R

class LoadingDialog(context: Context) {
    private val dialog = Dialog(context)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}
