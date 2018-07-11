package com.asd2.uaca.biclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.asd2.uaca.business.ApiCredentials
import com.asd2.uaca.data.Entry
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var apiCredentials :ApiCredentials

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init API credentials model
        apiCredentials = ApiCredentials(this)

        // Load all entries from web-service
        loadEntries()
    }

    override fun onResume() {
        // Verify when credentials were defined
        if(!apiCredentials.hasCredentialsDefined()) {
            Toast.makeText(this,getString(R.string.credentials_required),Toast.LENGTH_LONG).show()
            openSettingActivity()
        }

        return super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.item_settings -> {
                openSettingActivity()
            }
            R.id.item_reload -> {
                loadEntries()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadEntries() {
        Toast.makeText(this, getString(R.string.load_entires_message), Toast.LENGTH_LONG).show()
        Entry.loadEntries(this, txtView)
    }

    private fun openSettingActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }
}
