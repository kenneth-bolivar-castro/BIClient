package com.asd2.uaca.biclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
}
