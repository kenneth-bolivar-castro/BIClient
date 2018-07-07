package com.asd2.uaca.business

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.asd2.uaca.biclient.SettingsActivity
import org.json.JSONObject

class ApiCredentials(val context: Context) {

    var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    var bearerToken: String? = null

    fun hasCredentialsDefined(): Boolean {
        //
        if (preferences.getString(SettingsActivity.API_TOKEN_TEXT, "").isEmpty()) {
            return false
        } else if ("-1" == preferences.getString(SettingsActivity.ENV_LIST, "-1")) {
            return false
        }

        return true
    }

    fun getEntries(txtViewToken: TextView) {
        if (hasCredentialsDefined().not()) {
            return
        }

        val queue = Volley.newRequestQueue(context)
        val url = preferences.getString(SettingsActivity.ENV_LIST, null)
        Log.d(Log.DEBUG.toString(), url)

        val request = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    val result = JSONObject(response)
                    //
                    bearerToken = result.getString("access_token")

                    txtViewToken.text = bearerToken
                }, Response.ErrorListener { error ->
            Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
        }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                //
                val params = HashMap<String, String>()
                params["grant_type"] = "client_credentials"
                params["client_id"] = "aspnet-mvc-biclient-android"
                params["client_secret"] = preferences.getString(SettingsActivity.API_TOKEN_TEXT, null)

                return params
            }
        }

        queue.add(request)
    }
}
