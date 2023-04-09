package com.gaurav.heartone

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


var requestBody: MultipartBody? = null

class moodselection : Fragment(R.layout.fragment_moodselection) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moodselection, container, false)
    }

    override fun onStart() {
        super.onStart()
        val pictureButton = view?.findViewById<Button>(R.id.pictureAction)
        val sharedPreferences: SharedPreferences? = activity?.getSharedPreferences(
            "sharedPrefs",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences?.edit()
        val HttpClient = OkHttpClient()
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, Loading())
        pictureButton?.setOnClickListener() {
            if (pictureButton.text != "Continue") {
                openCamera()
            } else {
                Thread {
                    val request = requestBody?.let { it1 ->
                        Request.Builder()
                            .url("https://heartone.onrender.com/analyze")
                            .post(it1)
                            .build()
                    }
                    if (request != null) {
                        HttpClient.newCall(request).execute().use { response ->
                            val responseString = response.body?.string()
                            val responseMap:Map<*,*> = Gson().fromJson(responseString,Map::class.java) as Map<*, *>
                            editor?.apply{
                                putString("MOOD",findDominantAllowedEmotion(responseMap["emotion"] as Map<*, *>))
                            }?.apply()

                        }
                    }
                }.start()
                fragmentTransaction.commit()
            }
        }
        view?.findViewById<Button>(R.id.moodHappy)?.setOnClickListener() {
            editor?.apply {
                putString("MOOD","happy")
            }?.apply()
            fragmentTransaction.commit()
        }
        view?.findViewById<Button>(R.id.moodAngry)?.setOnClickListener(){
            editor?.apply {
                putString("MOOD","angry")
            }?.apply()
            fragmentTransaction.commit()
        }
        view?.findViewById<Button>(R.id.moodSad)?.setOnClickListener(){
            editor?.apply {
                putString("MOOD","sad")
            }?.apply()
            fragmentTransaction.commit()
        }
        view?.findViewById<Button>(R.id.moodNeutral)?.setOnClickListener(){
            editor?.apply {
                putString("MOOD","neutral")
            }?.apply()
            fragmentTransaction.commit()
        }

    }

    private fun findDominantAllowedEmotion(map : Map<*,*>): String {
        val ALLOWED_EMOTIONS = arrayOf(
            "happy",
            "sad",
            "angry",
            "neutral"
        )
        var maxConfidenceEmotion = String()
        var maxConfidence = 0.00
        for (emotion in map.keys){
            if (ALLOWED_EMOTIONS.contains(emotion)){
                println("$emotion with value ${map[emotion]}")
                if (map[emotion] as Double > maxConfidence){
                    maxConfidenceEmotion = emotion as String
                    maxConfidence = map[emotion] as Double
                }
            }
        }
        return maxConfidenceEmotion
    }

    private fun openCamera(){
        println("Camera Opened")
        if (Build.VERSION.SDK_INT > 23) {
            if (!hasPermissions()) {
                requestPermissions()
            }
        }
        val cameraIntent = Intent( MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(cameraIntent)
    }
    //this functions checks for permissions of CAMERA , READ and WRITE external storage at runtime for newer android APIs
    //ActivityCompat.requestPermissions is called for every permission that isn't granted the response for which is handled by following function
    val REQUEST_CODE_PERMISSIONS = 101
    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private fun hasPermissions(): Boolean {
        for (permission in PERMISSIONS) {
            if (context?.let { ContextCompat.checkSelfPermission(it, permission) }
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
    private fun requestPermissions() {
        activity?.let { ActivityCompat.requestPermissions(it, PERMISSIONS, REQUEST_CODE_PERMISSIONS) }
    }

    //function to handle the permission grant results corresponding to checkAndGetPermissions function

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera()
            }else{
                Toast.makeText(context,"Permission Denied",Toast.LENGTH_LONG).show()
            }
        }

    }


    //resultLauncher to launch the camera Intent to click a picture
    //once the result is fetched the image capture is set to the imageview on top of the button
    // TODO: Add a continue action after a picture is clicked.

    @SuppressLint("SetTextI18n")
    private  var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK){
            val imgView = view?.findViewById<ImageView>(R.id.imageView3)
            val picture = view?.findViewById<Button>(R.id.pictureAction)
            val photo:Bitmap = result.data?.extras?.get("data") as Bitmap
            imgView?.setImageBitmap( photo)
            saveBitmapToFile(photo)
            picture?.text = "Continue"
        }
    }
    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val file = File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "image.jpg")
        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            val byterarray = file.readBytes()
            requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.jpg", RequestBody.create("iamge/jpeg".toMediaTypeOrNull(),byterarray))
                    .build()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }


}