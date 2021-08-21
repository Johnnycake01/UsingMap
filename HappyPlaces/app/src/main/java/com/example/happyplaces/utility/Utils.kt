package com.example.happyplaces.utility

import android.view.View
import com.google.android.material.snackbar.Snackbar


    const val TAG = "mainActivity"
    const val CAMERA_PERMISSION_CODE = 1
    const val CAMERA_REQUEST_CODE = 2
    const val GALLERY_REQUEST_CODE = 3
    const val IMAGE_DIRECTORY = "HappyPLaceImages"
    const val DATABASE_NAME = "HappyPlaceDatabase"
    const val DATABASE_VERSION = 1
    const val HAPPY_PLACE_TABLE = "HappyPlaceTable"

    //ALL DATABASE COLUMN NAME
    const val KEY_ID = "_id"
    const val KEY_TITLE = "title"
    const val KEY_IMAGE = "image"
    const val KEY_DESCRIPTION = "description"
    const val KEY_DATE = "date"
    const val KEY_LOCATION = "location"
    const val KEY_LONGITUDE = "longitude"
    const val KEY_LATITUDE = "latitude"

        fun View.snackbar(message:String){
            Snackbar.make(
                this,
                message,
                Snackbar.LENGTH_LONG
            ).also {snackbar ->
                snackbar.setAction("ok"){
                    snackbar.dismiss()
                }

            }.show()
        }
