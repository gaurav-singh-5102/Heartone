package com.gaurav.heartone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        supportActionBar?.hide()

        val picture = findViewById<Button>(R.id.pictureAction)
        picture.setOnClickListener(){
            openCamera()
        }
    }
    
    // function that will be called when user clicks the pcitureAction buttton
    //first calls the checkAndGetPermission function then launches the Intent for
    // camera using resultLauncher
    
    private fun openCamera(){
        checkAndGetPermissions()
        val cameraIntent = Intent( MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(cameraIntent)
    }

    //this functions checks for permissions of CAMERA , READ and WRITE external storage at runtime for newer android APIs
    // TODO: Need to find a better way to implement this 
    //ActivityCompat.requestPermissions is called for every permission that isn't granted the response for which is handled by following function
    
    private fun checkAndGetPermissions() {
        if (ContextCompat.checkSelfPermission(this@AppActivity , Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@AppActivity, arrayOf(Manifest.permission.CAMERA),1337)
        }
        if (ContextCompat.checkSelfPermission(this@AppActivity,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@AppActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1338)
        }
        if (ContextCompat.checkSelfPermission(this@AppActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@AppActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1339)
        }
    }
    
    //function to handle the permission grant results corresponding to checkAndGetPermissions function

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1337){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this@AppActivity,"Permission Granted fpr Camera" ,Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == 1338){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this@AppActivity,"Permission Granted fpr Reading Storage" ,Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == 1337){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this@AppActivity,"Permission Granted fpr Writing Storage" ,Toast.LENGTH_SHORT).show()
            }
        }

    }

    //resultLauncher to launch the camera Intent to click a picture
    //once the result is fetched the image capture is set to the imageview on top of the button
    // TODO: Add a continue action after a picture is clicked. 

    private  var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == RESULT_OK){
            val imgView = findViewById<ImageView>(R.id.imageView3)
            val picture = findViewById<Button>(R.id.pictureAction)
            imgView.setImageBitmap( result.data?.extras?.get("data") as Bitmap)
            picture.text = "Continue"
        }
    }


}