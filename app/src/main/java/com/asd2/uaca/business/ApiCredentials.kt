package com.asd2.uaca.business

import android.content.Context
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONObject

class ApiCredentials(private val context: Context) {

    private val settings: Settings = Settings(context)

    fun hasCredentialsDefined(): Boolean {
        //
        if (settings.apiToken.isEmpty()) {
            return false
        } else if ("-1" == settings.endpoint) {
            return false
        }

        return true
    }

    fun setTokenAndRunCallback(callback: (bearer: String) -> Unit) {
        if (hasCredentialsDefined().not()) {
            return
        }

        // Setup parameters to consume TOKEN
        val params = hashMapOf(
                "grant_type" to GRANT_TYPE,
                "client_id" to CLIENT_ID,
                "client_secret" to settings.apiToken
        )

        // Create new httpClient
        val httpClient = HttpClient(context)

        // Setup url to retrieve TOKEN
        httpClient.url = settings.endpoint + API_TOKEN_PATH

        // Consume web-service by POST method
        httpClient.post(Response.Listener { response ->
            // Parse result into a JSON object
            val result = JSONObject(response)

            // Extract bearer token value
            callback(result.getString("access_token"))
        }, Response.ErrorListener {
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }, params)
    }

    companion object {
        const val API_TOKEN_PATH = "/api/token"
        const val GRANT_TYPE = "client_credentials"
        const val CLIENT_ID = "aspnet-mvc-biclient-android"
    }
}
