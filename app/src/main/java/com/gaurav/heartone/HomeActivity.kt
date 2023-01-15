package com.gaurav.heartone


// Activity to Authenticate the user using spotify auth library

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationClient.getResponse
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        //when login button is clicked an Authentication Request is built

        val login = findViewById<Button>(R.id.buttonLogin)
        login.setOnClickListener(){
            val request = AuthorizationRequest.Builder(
                SpotifyConstants.CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                SpotifyConstants.REDIRECT_URI
            ).setScopes(
                arrayOf(
                    "user-read-private",
                    "playlist-read",
                    "playlist-read-private",
                    "streaming",
                    "app-remote-control",
                    "user-top-read"
                )
            ).build()

            //creating login activity intent to be used with a result Lanucher

            val intent = AuthorizationClient.createLoginActivityIntent(this, request)
            resultLauncher.launch(intent)
        }

    }
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK){
            val data: Intent? = result.data
            // There are no request code
            val response = getResponse(result.resultCode, data)
            val sharedPreferences : SharedPreferences? = getSharedPreferences("sharedPrefs",
                Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            if (response.type == AuthorizationResponse.Type.TOKEN){
                editor?.apply {
                    putString("ACCESS_TOKEN",response.accessToken)
                }?.apply()
                val i = Intent(this@HomeActivity , AppActivity::class.java)
                startActivity(i)
                finish()
            }
            else{
                Toast.makeText(this@HomeActivity,"Authorization wasn't completed",Toast.LENGTH_LONG)
            }

        }
    }

}

