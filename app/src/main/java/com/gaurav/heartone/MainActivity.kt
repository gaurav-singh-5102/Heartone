package com.gaurav.heartone

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

//Activity that acts as a SplashScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //code to check if the user had already logged in
        //in case already logged in then skip the Login activity (Home)
        //in case not logged in then continue the normal flow

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN",null)
        val i = if (accessToken==null){
            Intent(this@MainActivity , HomeActivity::class.java)
        }else {
            Intent(this@MainActivity , AppActivity::class.java)
        }

        Handler().postDelayed({
            startActivity(i)
            finish()
        },1500)
        supportActionBar?.hide()

    }
}