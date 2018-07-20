package com.asd2.uaca.biclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.asd2.uaca.data.Entry
import kotlinx.android.synthetic.main.activity_entry.*

class EntryActivity : AppCompatActivity() {

    private lateinit var entry: Entry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)


        val adapter = ArrayAdapter.createFromResource(
                this,
                R.array.list_entry_statuses,
                android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinStatuses.adapter = adapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        entry = intent.getSerializableExtra(Entry.CURRENT_ENTRY) as Entry
//        Toast.makeText(this,
//                entry.item,
//                Toast.LENGTH_LONG
//        ).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            startActivity(Intent(this, MainActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
