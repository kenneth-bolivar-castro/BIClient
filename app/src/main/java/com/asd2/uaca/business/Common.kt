package com.asd2.uaca.business

import android.content.Context
import android.widget.TextView
import com.asd2.uaca.biclient.R

class Common {

    companion object {

        fun showErrorMessage(context: Context, txtView: TextView, message: String?) {
            var text = context.resources.getString(R.string.error_http)
            if(null != message) {
                text += message
            }
            txtView.text = text
        }
    }
}