package com.asd2.uaca.business

import android.content.Context
import android.widget.TextView
import com.android.volley.Response
import org.json.JSONArray

abstract class AbstractManager(protected val context: Context,
                               protected val apiCredentials: ApiCredentials) {

    protected var settings: Settings = Settings(context)
    lateinit var txtView: TextView
    open lateinit var fullUrl: String

    protected fun getResponseErrorListener(): Response.ErrorListener {
        return Response.ErrorListener {
            Common.showErrorMessage(context, txtView, it.message)
        }
    }

    abstract fun findAll()

    abstract fun fillOut(result: JSONArray)

    abstract fun mergeEntity(entity: Any, callback: (entity: Any) -> Unit)
}
