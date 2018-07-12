package com.asd2.uaca.business

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class HttpClient(private val context: Context) {

    lateinit var url: String
    var authorization: String? = null

    fun get(listener: Listener<String>, errorListener: ErrorListener) {
        //
        val request = object : StringRequest(
                Request.Method.GET,
                url,
                listener,
                errorListener
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // When authorization was defined
                if (null != authorization) {
                    return getHeaders(authorization!!)
                }

                // Returns parent headers
                return super.getHeaders()
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    fun post(listener: Listener<String>, errorListener: ErrorListener,
             params: HashMap<String, String>? = null) {
        //
        val request = object : StringRequest(
                Request.Method.POST,
                url,
                listener,
                errorListener
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // When authorization was defined
                if (null != authorization) {
                    return getHeaders(authorization!!)
                }

                // Returns parent headers
                return super.getHeaders()
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    companion object {

        private fun getHeaders(authorization: String): HashMap<String, String> {
            // Setup headers properly
            return hashMapOf(
                    "Content-Type" to "application/json",
                    "Authorization" to "Bearer $authorization"
            )
        }
    }
}