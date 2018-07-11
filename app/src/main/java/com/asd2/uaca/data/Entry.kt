package com.asd2.uaca.data

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.asd2.uaca.business.ApiCredentials
import com.asd2.uaca.business.HttpClient
import com.asd2.uaca.business.Settings
import org.json.JSONObject
import java.util.*

class Entry(val key: Int, val created: Date, val comments: String, val item: String, val client: Client, val userId: String) {

    companion object {
        private const val ENTRY_PATH = "/api/values"

        fun loadEntries(context: Context, txtView: TextView) {
            //
            val apiCredentials = ApiCredentials(context)
            apiCredentials.setTokenAndRunCallback {
                // Create new httpClient
                val httpClient = HttpClient(context)

                // Setup url to retrieve TOKEN
                httpClient.url = Settings(context).endpoint + ENTRY_PATH

                // Setup authorize attribute
                httpClient.authorization = it

                // Consume web-service by POST method
                httpClient.get(Response.Listener { response ->
                    // Parse result into a JSON object
//                    val result = JSONObject(response)

                    // Expose result within text view
                    txtView.text = response.toString()
                }, Response.ErrorListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                })
            }
        }
    }
}