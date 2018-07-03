package com.asd2.uaca.biclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTabMe = findViewById<Button>(R.id.btnTabMe)
        btnTabMe.setOnClickListener{
            val editText = findViewById<EditText>(R.id.editText)
            Toast.makeText(this, editText.text.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        var token = preferences.getString(SettingsActivity.API_TOKEN_TEXT, null)
        if(null == token) {
            Toast.makeText(this,"API Token es requerido",Toast.LENGTH_LONG).show()
            openSettingActivity()
        }

        var env = preferences.getString(SettingsActivity.ENV_LIST, null)
        if (null == env) {
            Toast.makeText(this,"Ambiente es requerido",Toast.LENGTH_LONG).show()
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openSettingActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }
}
