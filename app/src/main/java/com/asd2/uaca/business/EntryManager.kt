package com.asd2.uaca.business

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.android.volley.Response
import com.asd2.uaca.data.Entry
import org.json.JSONArray

class EntryManager(private val context: Context,private val apiCredentials: ApiCredentials) {

    lateinit var txtView: TextView
    lateinit var listView: ListView
    var entryIds = intArrayOf(1, 2)

    private fun fillOutListView(result: JSONArray) {
        //
        val entries = arrayOf(
                "Cocina de gas con horno",
                "Aire acondicionado 12 000 VTU"
        )

        listView.adapter = ArrayAdapter<String>(context,
                R.layout.simple_list_item_1,
                entries
        )
    }

    fun findAll() {
        // Invoke callback after retrieve authentication token.
        apiCredentials.setTokenAndRunCallback {
            // Create new httpClient
            val httpClient = HttpClient(context)

            // Setup url to retrieve TOKEN
            httpClient.url = Settings(context).endpoint + Entry.ENTRY_PATH

            // Setup authorize attribute
            httpClient.authorization = it

            // Consume web-service by POST method
            httpClient.get(Response.Listener { response ->
                // Parse result into a JSON object
                val result = JSONArray(response)

                //
                fillOutListView(result)

                // Expose result within text view
                txtView.text = response.toString()
            }, Response.ErrorListener {
                Common.showErrorMessage(context, txtView, it.message)
            })
        }
    }
}
