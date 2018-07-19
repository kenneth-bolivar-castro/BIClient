package com.asd2.uaca.business

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import com.android.volley.Response
import org.json.JSONObject

class ApiCredentials(private val context: Context) {

    private val settings: Settings = Settings(context)
    lateinit var txtView: TextView

    fun cleanUpTokenAccessAt(expiresInSeconds: Long) {
        // Setup countdown time to remove access token stored
        object : CountDownTimer(
                expiresInSeconds * 1000,
                1000) {
            override fun onTick(millisInFuture: Long) {
                Log.i(Log.INFO.toString(), millisInFuture.toString())
            }
            override fun onFinish() {
                // Remove access token from settings.
                settings.accessToken = null
            }
        }.start()
    }

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

        // When access token is already define, then execute callback
        if (null != settings.accessToken) {
            callback(settings.accessToken!!)
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

            // Extract access token
            var accessToken = result.getString("access_token")

            // Setup access token into settings
            settings.accessToken = accessToken

            // Setup time to clean up access token
            cleanUpTokenAccessAt(result.getLong("expires_in"))

            // Extract bearer token value
            callback(accessToken)
        }, Response.ErrorListener {
            Common.showErrorMessage(context, txtView, it.message)
        }, params)
    }

    companion object {

        const val API_TOKEN_PATH = "/api/token"
        const val GRANT_TYPE = "client_credentials"
        const val CLIENT_ID = "aspnet-mvc-biclient-android"
    }
}
