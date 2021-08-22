package com.example.happyplaces.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R
import com.example.happyplaces.adapter.PlacesAdapter
import com.example.happyplaces.database.DatabaseHandler
import com.example.happyplaces.models.HappyPlaceModel
import com.example.happyplaces.utility.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var addHappyPlaceButton:FloatingActionButton
    private lateinit var rv:RecyclerView
    private lateinit var  rvAdapter:PlacesAdapter
    private lateinit var tv:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addHappyPlaceButton = findViewById(R.id.faAddNewPlace)
        rv = findViewById(R.id.recyclerView)
        tv = findViewById(R.id.rvText)
//
        addHappyPlaceButton.setOnClickListener {
            val intent = Intent(this, AddNewHappyPlace::class.java)
            startActivityForResult(intent, ADD_NEW_PLACE_CODE)
        }
            gtHappyPlaceListFunc()
    }
    private fun gtHappyPlaceListFunc(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList:ArrayList<HappyPlaceModel> = dbHandler.getHappyPlace()
        if (getHappyPlaceList.size > 0){
            rv.visibility = View.VISIBLE
            tv.visibility = View.GONE
          updateRecyclerView(getHappyPlaceList)

        }else{
            rv.visibility = View.GONE
            tv.visibility = View.VISIBLE

        }
    }

    private fun updateRecyclerView(array:ArrayList<HappyPlaceModel>) {
        rv.layoutManager = LinearLayoutManager(this)
        rvAdapter = PlacesAdapter(this,array)
        rv.setHasFixedSize(true)
        rv.adapter = rvAdapter
        rvAdapter.notifyDataSetChanged()
        rvAdapter.setOnClickListener(object:PlacesAdapter.OnClickOfHappyPlace{
            override fun onClick(position: Int, itemPosition: HappyPlaceModel) {
                val intent = Intent(this@MainActivity,HappyPlalceDetails::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS,itemPosition)
                startActivity(intent)
            }

        } )
        val editSwapHandler = object :SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv.adapter as PlacesAdapter
                adapter.notifyEditItem(this@MainActivity,viewHolder.adapterPosition, ADD_PLACE_ACTIVITY_REQUEST_CODE)
                gtHappyPlaceListFunc()
            }

        }
        val editItemTouchHelper = ItemTouchHelper(editSwapHandler)
        editItemTouchHelper.attachToRecyclerView(rv)

        //delete
        val deleteSwapHandler = object :SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv.adapter as PlacesAdapter
                adapter.delteItemAt(viewHolder.adapterPosition)
                gtHappyPlaceListFunc()

            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwapHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NEW_PLACE_CODE){
            if (resultCode == Activity.RESULT_OK){
                gtHappyPlaceListFunc()
            }
        }
    }
}