package com.asd2.uaca.business

import android.content.Context
import android.content.Entity
import android.widget.TextView
import com.asd2.uaca.data.Client
import org.json.JSONArray

abstract class AbstractManager(protected val context: Context,
                               protected val apiCredentials: ApiCredentials) {

    protected var settings: Settings = Settings(context)
    lateinit var txtView: TextView

    abstract fun findAll()

    abstract fun fillOut(result: JSONArray)

    abstract fun mergeEntity(entity: Any, callback: (entity: Any) -> Unit)
}
