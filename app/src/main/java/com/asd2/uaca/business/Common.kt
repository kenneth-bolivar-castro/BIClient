package com.asd2.uaca.business

import android.content.Context
import android.widget.TextView
import com.asd2.uaca.biclient.R

class Common {

    companion object {

        fun showErrorMessage(context: Context, txtView: TextView, message: String?) {
            val text = context.resources.getString(R.string.error_http) + message
            txtView.text = text
        }
    }
}