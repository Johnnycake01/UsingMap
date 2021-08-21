package com.example.happyplaces.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.happyplaces.R
import com.example.happyplaces.database.DatabaseHandler
import com.example.happyplaces.models.HappyPlaceModel
import com.example.happyplaces.utility.*
import com.google.android.material.textfield.TextInputEditText
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class AddNewHappyPlace : AppCompatActivity(), View.OnClickListener {
    private lateinit var addHappyPlaceToolBar:androidx.appcompat.widget.Toolbar
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
    private lateinit var myDate: TextInputEditText
    private lateinit var description: TextInputEditText
    private lateinit var title: TextInputEditText
    private lateinit var location: TextInputEditText
    private lateinit var ivImage:ImageView
    private lateinit var thisLayout:ConstraintLayout
    private var saveImageToGallery:Uri? = null
    private  var mLatitude:Double = 0.0
    private var mLongitude:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_happy_place)
        Log.d(TAG, "onCreate")

        description = findViewById(R.id.teDescription)
        title = findViewById(R.id.teTitle)
        location = findViewById(R.id.teLocation)
        addHappyPlaceToolBar = findViewById(R.id.addHappyPlaceToolBar)
        myDate = findViewById(R.id.teDate)
        ivImage = findViewById(R.id.ivImage)
        thisLayout = findViewById(R.id.addNewPlace)

        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        cal.set(Calendar.YEAR,year)
            cal.set(Calendar.MONTH,month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        updateDateInView() //to auto input current date and assert that date field is not null
        setSupportActionBar(addHappyPlaceToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addHappyPlaceToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
//set onclick listener for all views
    override fun onClick(v: View?) {

        when(v!!.id){
                R.id.teDate ->{
                    DatePickerDialog(
                        this@AddNewHappyPlace,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)

                    ).show()
                }
            R.id.tvAddImage ->{
                Log.d(TAG, "botton clicked")
                val addPictureDialogue = AlertDialog.Builder(this)
                addPictureDialogue.setTitle("select Action")
                val addPictureDialogueItem = arrayOf(
                    "Select picture from gallery","Capture photo from camera")
                addPictureDialogue.setItems(addPictureDialogueItem){
                    _,which ->
                    when(which){
                        0 -> {
                            choosePhotoFromGallery()
                        }
                        1 -> {
                            openCamera()
                        }
                    }
                }
                addPictureDialogue.show()
            }
            R.id.btSave ->{
                when{
                    title.text.isNullOrEmpty()->{
                        title.error = "title field cannot be empty"
                    }
                    description.text.isNullOrEmpty()->{
                        description.error = "description field cannot be empty"
                    }
                    location.text.isNullOrEmpty()->{
                        location.error = "location field cannot be empty"
                    }
                    saveImageToGallery == null->{
                        thisLayout.snackbar("please select an image")
                    }
                    else -> {
                        val happyPlaceModel = HappyPlaceModel(0,
                            title.text.toString(),
                            description.text.toString(),
                            myDate.text.toString(),
                            location.text.toString(),
                            saveImageToGallery.toString(),
                            mLatitude,mLongitude)
                        val dbHandler = DatabaseHandler(this)
                        val addHappyPlaceToDB = dbHandler.addHappyPlace(happyPlaceModel)
                        if(addHappyPlaceToDB > 0){
                            thisLayout.snackbar("Happy Place added successfully")
                        }

                    }

                }

            }
        }

    }

    private fun openCamera() {
        checkIfCameraPermissionIsGranted()
    }

    private fun checkIfCameraPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }else{
            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }else{
                thisLayout.snackbar("Permission Denied")

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_REQUEST_CODE){
                val thumbNail:Bitmap = data!!.extras!!.get("data") as Bitmap
                ivImage.setImageBitmap(thumbNail)
                 saveImageToGallery = saveImageToInternalStorage(thumbNail)
                Log.d(TAG,"gallery uri:::$saveImageToGallery")

            }
            if (requestCode == GALLERY_REQUEST_CODE){
                if(data != null){
                    val imageUri = data.data
                    try{
                        val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,imageUri)
                        saveImageToGallery = saveImageToInternalStorage(imageBitmap)
                        Log.d(TAG,"gallery uri:::$saveImageToGallery")
                        Glide.with(this)
                            .load(imageUri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivImage)
                    }catch (e:IOException){
                        e.printStackTrace()
                        thisLayout.snackbar("image could not be loaded")
                    }
                }

            }
        }

    }
    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object :MultiplePermissionsListener{
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                if (p0!!.areAllPermissionsGranted()){
                    val galleryIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(galleryIntent,GALLERY_REQUEST_CODE)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
                showRationalCaseDialogue()
            }

        }).onSameThread().check()
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap):Uri{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")
        try {
            val stream:OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    private fun showRationalCaseDialogue() {
        AlertDialog.Builder(this)
            .setMessage("it seems like you have turned off permission required for this feature." +
                    "it acn be enabled under application settings")
            .setPositiveButton("Go to settings"){_,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e:ActivityNotFoundException){
                    e.printStackTrace()
                }

            }
            .setNegativeButton("Back"){dialog,_ ->
                dialog.dismiss()
            }.show()
    }

    private  fun updateDateInView(){
        val format = "dd-MM-yyyy"
        val dateFormat = SimpleDateFormat(format,Locale.getDefault())
        myDate.setText(dateFormat.format(cal.time).toString())

    }
}