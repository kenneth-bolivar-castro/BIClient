package com.asd2.uaca.business

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.ArrayAdapter
import android.widget.ListView
import com.android.volley.Response
import com.asd2.uaca.data.Client
import com.asd2.uaca.data.Entry
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.util.*

class EntryManager(context: Context,
                   apiCredentials: ApiCredentials)
    : AbstractManager(context, apiCredentials) {

    lateinit var listView: ListView
    var entries: ArrayList<Entry> = ArrayList()
    override var fullUrl = settings.endpoint + Entry.ENTRY_PATH

    private fun getResponseListener(callback: (entity: Any) -> Unit): Response.Listener<String> {
        return Response.Listener {
            // Parse result into a JSON object
            val result = JSONObject(it)

            // Execute given callback function
            callback(result)
        }
    }

    private fun resolveClient(item: JSONObject): Client {
        //
        val client = Client(item.getInt("Id"))
        client.fullname = item.getString("Name")
        client.dni = item.getString("DNI")
        client.phoneNumber = item.getString("Phone")
        client.email = item.getString("Email")

        return client
    }

    private fun resolveEntries(result: JSONArray) {
        // Get through all result items
        for (i in 0..(result.length() - 1)) {
            // Extract item from current index
            val item = result.getJSONObject(i)

            // Retrieve primary key and status
            val itemKey = item.getInt("Id")
            val status = item.getInt("Status")

            // Lookup for current entry into collection
            var entry = entries.find { e -> e.key == itemKey }
            if(null == entry) {
                //
                entry = Entry(itemKey, Entry.getStatus(status))
            } else {
                entry.status = Entry.getStatus(status)
            }

            //
            val pieces = item.getString("Date").split("T")
            val date = pieces.first().split("-")

            //
            entry.created = String.format("%s-%s-%s", date[2], date[1], date[0])
            entry.comments = item.getString("Comments")
            entry.item = item.getString("Item")
            entry.client = resolveClient(item.getJSONObject("Client"))

            //
            val idx = entries.indexOf(entry)
            if(-1 == idx) {
                //
                entries.add(entry)
            } else {
                entries[idx] = entry
            }
        }
    }

    override fun fillOut(result: JSONArray) {
        //
        resolveEntries(result)

        //
        val items = ArrayList<String>()

        // Grab all entry names
        for (entry in entries) {
            // Add new entry item value
            items.add(entry.item)
        }

        // Inject adapter to list view
        listView.adapter = ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1,
                items
        )
    }

    override fun findAll() {
        // Invoke callback after retrieve authentication token.
        apiCredentials.setTokenAndRunCallback {
            // Create new httpClient
            val httpClient = Common.getHttpClient(context, it, fullUrl)

            // Consume web-service by GET method
            httpClient.get(Response.Listener { response ->
                // Parse result into a JSON object
                val result = JSONArray(response)

                // Expose result within text view
                fillOut(result)

                // Clean up txtView
                txtView.text = null
            }, getResponseErrorListener())
        }
    }

    override fun mergeEntity(entity: Any, callback: (entity: Any) -> Unit) {
        //
        val entry = entity as Entry

        //
        apiCredentials.setTokenAndRunCallback {
            // Verify when it is a new entry
            val isNew = (0 == entry.key)

            // Define proper endpoint URL
            var endpointUrl = fullUrl
            if(!isNew) {
                endpointUrl = "${fullUrl}/${entry.key}"
            }

            // Get httpClient instance
            val httpClient = Common.getHttpClient(context, it, endpointUrl)

            //
            val result: List<String> = entry.created.split("-")
            val date = "${result.last().toInt()}-${result[1].toInt()}-${result.first().toInt()}"

            // Setup parameters to create a client
            val params = hashMapOf(
                    "Date" to "${date}T00:00:00.00",
                    "Comments" to entry.comments,
                    "Item" to entry.item,
                    "Status" to Entry.getStatusIndex(entry.status).toString(),
                    "ClientId" to entry.client.key.toString()
            )

            // When it is a new entry
            if(isNew) {
                // Consume web-service by POST method
                httpClient.post(
                        getResponseListener(callback),
                        getResponseErrorListener(),
                        params
                )
            } else {
                // Include Id parameter value
                params["Id"] = entry.key.toString()

                // Consume web-service by PUT method
                httpClient.put(
                        getResponseListener(callback),
                        getResponseErrorListener(),
                        params
                )
            }
        }
    }
}
