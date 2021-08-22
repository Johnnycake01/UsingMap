package com.example.happyplaces.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.happyplaces.R
import com.example.happyplaces.models.HappyPlaceModel
import com.example.happyplaces.utility.EXTRA_PLACE_DETAILS

class HappyPlalceDetails : AppCompatActivity() {
    private lateinit var addHappyPlaceToolBar: Toolbar
    private lateinit var ivImage:ImageView
    private lateinit var tvlocation:TextView
    private lateinit var tvDescription:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_plalce_details)
        addHappyPlaceToolBar = findViewById(R.id.toolbar_happy_place_detail)
        ivImage = findViewById(R.id.iv_place_image)
        tvDescription = findViewById(R.id.tv_description)
        tvlocation = findViewById(R.id.tv_location)

        var happyPlalceDetailsModel:HappyPlaceModel? = null
        if (intent.hasExtra(EXTRA_PLACE_DETAILS)){
            happyPlalceDetailsModel = intent.getParcelableExtra(EXTRA_PLACE_DETAILS)
        }
        if (happyPlalceDetailsModel != null){
            setSupportActionBar(addHappyPlaceToolBar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = happyPlalceDetailsModel.title
            addHappyPlaceToolBar.setNavigationOnClickListener {
                onBackPressed()
            }
            ivImage.setImageURI(Uri.parse(happyPlalceDetailsModel.image))
            tvDescription.text = happyPlalceDetailsModel.description
            tvlocation.text = happyPlalceDetailsModel.location
        }

    }
}