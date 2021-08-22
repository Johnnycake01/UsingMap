package com.example.happyplaces.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R
import com.example.happyplaces.models.HappyPlaceModel

class PlacesAdapter (private val context: Context, private val listItem:ArrayList<HappyPlaceModel>):
    RecyclerView.Adapter<PlacesAdapter.ContactHolder>(){

    private var onClickListener:OnClickOfHappyPlace? = null

    //inner class of recycler view that extends View holder
    inner class ContactHolder(contactView: View): RecyclerView.ViewHolder(contactView){
        val rvImage: ImageView = contactView.findViewById(R.id.rvImage)
        val rvTitle: TextView = contactView.findViewById(R.id.rvTitleText)
        val rvDescription: TextView = contactView.findViewById(R.id.rvDescriptionText)

    }

    //on create of view holder the recycler view adapter should inflate the view layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.places_list_layout,parent,false)
        return  ContactHolder(view)
    }

    //this binds the view together
    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val itemPosition = listItem[position]
        holder.rvTitle.text = itemPosition.title
        holder.rvDescription.text = itemPosition.description
       holder.rvImage.setImageURI(Uri.parse(itemPosition.image))

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, itemPosition)
            }
        }


    }
    fun setOnClickListener(onClickListener: OnClickOfHappyPlace) {
        this.onClickListener = onClickListener
    }
    interface OnClickOfHappyPlace {

        fun onClick(position: Int, itemPosition: HappyPlaceModel)

    }

    //function to get count of item
    override fun getItemCount(): Int {
        return listItem.size
    }


}