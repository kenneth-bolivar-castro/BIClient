package com.asd2.uaca.biclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.asd2.uaca.business.ApiCredentials
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter
import com.asd2.uaca.business.EntryManager
import com.asd2.uaca.data.Entry
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var apiCredentials: ApiCredentials
    private lateinit var entryManager: EntryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init attributes
        initialization()

        // Load all entries from web-service
        loadEntries()
    }

    override fun onResume() {
        // Verify when credentials were defined
        if (apiCredentials.hasCredentialsDefined().not()) {
            Toast.makeText(this,
                    getString(R.string.credentials_required),
                    Toast.LENGTH_LONG
            ).show()
            openSettingActivity()
        }

        return super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_settings -> {
                openSettingActivity()
            }
            R.id.item_reload -> {
                loadEntries()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialization() {
        // Init ApiCredentials attribute
        apiCredentials = ApiCredentials(this)
        apiCredentials.cleanUpTokenAccessAt(0)
        apiCredentials.txtView = txtView

        // Init EntryManager attribute
        entryManager = EntryManager(this, apiCredentials)
        entryManager.txtView = txtView
        entryManager.listView = listViewEntries
    }

    private fun loadEntries() {
        // Show message to aware user about current process
        Toast.makeText(this,
                getString(R.string.load_entires_message),
                Toast.LENGTH_LONG
        ).show()

        entryManager.findAll()

        listViewEntries.setOnItemClickListener { parent, view, index, id ->
            //
            val intent = Intent(this, EntryActivity::class.java)

            //
            //intent.putExtra(Entry.CURRENT_ENTRY, entryManager.entries[index] as Serializable)

            //
            startActivity(intent)
        }
    }

    private fun openSettingActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }
}
