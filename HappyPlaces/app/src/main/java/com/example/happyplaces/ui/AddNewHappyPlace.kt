package com.example.happyplaces.ui

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.example.happyplaces.R
import java.util.*

class AddNewHappyPlace : AppCompatActivity() {
    private lateinit var addHappyPlaceToolBar:androidx.appcompat.widget.Toolbar
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_happy_place)
        addHappyPlaceToolBar = findViewById(R.id.addHappyPlaceToolBar)
        setSupportActionBar(addHappyPlaceToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addHappyPlaceToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}