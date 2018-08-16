package com.asd2.uaca.biclient

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.asd2.uaca.business.ApiCredentials
import com.asd2.uaca.business.ClientManager
import com.asd2.uaca.business.EntryManager
import com.asd2.uaca.data.Client
import com.asd2.uaca.data.Entry
import kotlinx.android.synthetic.main.activity_entry.*
import java.text.SimpleDateFormat
import java.util.*

class EntryActivity : AppCompatActivity() {

    private lateinit var apiCredentials: ApiCredentials
    private lateinit var clientManager: ClientManager
    private lateinit var entryManager: EntryManager
    private lateinit var entry: Entry
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        //
        btnSave.setOnClickListener {
            val clientDNI = auTxtDNI.text.toString()
            var client = clientManager.clients.find { c -> c.dni.equals(clientDNI) }
            if(null == client) {
               client = Client(0)
            }
            client.fullname = txtClientName.text.toString()
            client.dni = clientDNI
            client.phoneNumber = txtClientPhone.text.toString()
            client.email = txtClientEmail.text.toString()

            // On new client
            if(0 == client.key) {
                // Insert new client then merge entry
                addNewClient(client)
            } else {
                // Merge entry
                mergeEntry(client)
            }
        }

        //
        initialization()

        //
        initializeElements()

        //
        setUpCurrentEntry()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            startActivity(Intent(this, MainActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewClient(client: Client) {
        //
        clientManager.mergeEntity(client) {
            val newClient = it as Client
            clientManager.clients.add(newClient)
            mergeEntry(newClient)
        }
    }

    private fun mergeEntry(client: Client) {
        //
        Log.d(Log.DEBUG.toString(), client.toString())
    }

    private fun initialization() {
        // Init ApiCredentials attribute
        apiCredentials = ApiCredentials(this)
        apiCredentials.cleanUpTokenAccessAt(0)
        apiCredentials.txtView = txtView

        // Init ClientManager attribute
        clientManager = ClientManager(this, apiCredentials)
        clientManager.txtView = txtView
        clientManager.autoCompleteTextView = auTxtDNI

        // Init EntryManager attribute
        entryManager = EntryManager(this, apiCredentials)
        entryManager.txtView = txtView
    }

    private fun initializeElements() {
        setSpinnerOptions()
        setDatePickerCalendar()
        setAutocomplete()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setSpinnerOptions() {
        val adapter = ArrayAdapter.createFromResource(
                this,
                R.array.list_entry_statuses,
                android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinStatuses.adapter = adapter
    }

    private fun formatDate(year:Int, month:Int, day:Int): String {
        // Create a Date variable/object with user chosen date
        calendar.set(year, month, day, 0, 0, 0)
        val chosenDate = calendar.time

        // Format the date picker selected date
        return SimpleDateFormat("dd-MM-yyyy").format(chosenDate)
    }

    private fun setDatePickerCalendar() {
        //
        calendar = Calendar.getInstance()

        //
        txtDate.setOnFocusChangeListener { view, isFocus ->
            //
            if(isFocus.not()) {
                return@setOnFocusChangeListener
            }

            // Get the system current date
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            //
            DatePickerDialog(
                    this,
                    android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth,
                    DatePickerDialog.OnDateSetListener { _: DatePicker,
                                                         selectedYear: Int,
                                                         selectedMonth: Int,
                                                         selectedDay: Int ->
                        // Setup chose date
                        calendar.set(selectedYear, selectedMonth, selectedDay)

                        // Setup format chose date
                        val date = formatDate(selectedYear, selectedMonth, selectedDay)
                        txtDate.setText(date)

                        // Move focus to another field
                        auTxtDNI.requestFocus()
                    },
                    year,
                    month,
                    day
            ).show()
        }
    }

    private fun setAutocomplete() {
        //
        clientManager.findAll()

        //
        auTxtDNI.setOnItemClickListener { _, _, i, _ ->
            //
            val client = clientManager.clients[i]
            txtClientName.setText(client.fullname)
            txtClientPhone.setText(client.phoneNumber)
            txtClientEmail.setText(client.email)
        }
    }

    private fun setUpCurrentEntry() {
        //
        entry = intent.getSerializableExtra(Entry.CURRENT_ENTRY) as Entry

        // If entry.key is zero, then disappear status dropdown
        if (0 == entry.key) {
            txtStatusLabel.visibility = View.GONE
            spinStatuses.visibility = View.GONE
        } else {
            // Populate entry info
            txtDate.setText(entry.created)
            auTxtDNI.setText(entry.client.dni)
            txtClientName.setText(entry.client.fullname)
            txtEntryItem.setText(entry.item)
            txtEntryComments.setText(entry.comments)

            // Otherwise setup status value
            spinStatuses.setSelection(Entry.getStatusIndex(entry.status))
        }
    }
}
