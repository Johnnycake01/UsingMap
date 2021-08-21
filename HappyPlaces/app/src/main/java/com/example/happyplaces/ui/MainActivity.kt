package com.example.happyplaces.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaces.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var addHappyPlaceButton:FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addHappyPlaceButton = findViewById(R.id.faAddNewPlace)

        addHappyPlaceButton.setOnClickListener {
            val intent = Intent(this, AddNewHappyPlace::class.java)
            startActivity(intent)
        }

    }
}