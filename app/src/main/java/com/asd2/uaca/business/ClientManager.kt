package com.asd2.uaca.business

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.android.volley.Response
import com.asd2.uaca.data.Client
import org.json.JSONArray
import org.json.JSONObject

class ClientManager(context: Context,
                    apiCredentials: ApiCredentials)
    : AbstractManager(context, apiCredentials) {

    lateinit var autoCompleteTextView: AutoCompleteTextView
    var clients: ArrayList<Client> = ArrayList()

    private fun resolveClients(result: JSONArray) {
        // Get through all result items
        for (i in 0..(result.length() - 1)) {
            // Extract item from current index
            val item = result.getJSONObject(i)

            // Retrieve primary key and status
            val itemKey = item.getInt("Id")

            // Lookup for current client into collection
            var client = clients.find { c -> c.key == itemKey }
            if (null == client) {
                client = Client(itemKey)
            }

            client.fullname = item.getString("Name")
            client.dni = item.getString("DNI")
            client.phoneNumber = item.getString("Phone")
            client.email = item.getString("Email")

            //
            clients.add(client)
        }
    }

    private fun getHttpClient(context: Context, authorization: String): HttpClient {
        // Create new httpClient
        val httpClient = HttpClient(context)

        // Setup url to retrieve TOKEN
        httpClient.url = settings.endpoint + Client.CLIENT_PATH

        // Setup authorize attribute
        httpClient.authorization = authorization

        return httpClient
    }

    override fun fillOut(result: JSONArray) {
        //
        resolveClients(result)

        //
        val suggestions = ArrayList<String>()

        // Grab all client DNI values
        for (client in clients) {
            // Add new client DNI value
            suggestions.add(client.dni)
        }

        // Inject adapter to autoCompleteTextView
        autoCompleteTextView.setAdapter(ArrayAdapter(context,
                android.R.layout.simple_list_item_1,
                suggestions))
        autoCompleteTextView.threshold = 3
    }

    override fun findAll() {
        // Invoke callback after retrieve authentication token.
        apiCredentials.setTokenAndRunCallback {
            // Get httpClient instance
            val httpClient = getHttpClient(context, it)

            // Consume web-service by GET method
            httpClient.get(Response.Listener { response ->
                // Parse result into a JSON object
                val result = JSONArray(response)

                // Expose result within text view
                fillOut(result)

                // Clean up txtView
                txtView.text = null
            }, Response.ErrorListener {
                Common.showErrorMessage(context, txtView, it.message)
            })
        }
    }

    override fun mergeEntity(entity: Any, callback: (entity: Any) -> Unit) {
        //
        val client = entity as Client

        //
        apiCredentials.setTokenAndRunCallback {
            // Get httpClient instance
            val httpClient = getHttpClient(context, it)

            // Setup parameters to create a client
            val params = hashMapOf(
                    "Name" to client.fullname,
                    "DNI" to client.dni,
                    "Phone" to client.phoneNumber,
                    "Email" to client.email
            )

            // Consume web-service by POST method
            httpClient.post(Response.Listener { response ->
                // Parse result into a JSON object
                val result = JSONObject(response)

                // Setup access token into settings
                val clientId = result.getInt("Id")!!

                // Setup client instance
                val newClient = client.copy(key = clientId)
                newClient.fullname = client.fullname
                newClient.dni = client.dni
                newClient.phoneNumber = client.phoneNumber
                newClient.email = client.email

                // Extract bearer token value
                callback(newClient)
            }, Response.ErrorListener {
                Common.showErrorMessage(context, txtView, it.message)
            }, params)


        }
    }
}