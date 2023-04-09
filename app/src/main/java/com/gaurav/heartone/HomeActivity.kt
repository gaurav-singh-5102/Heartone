package com.gaurav.heartone


// Activity to Authenticate the user using spotify auth library

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.gaurav.heartone.repository.AppDatabase
import com.gaurav.heartone.repository.PlaylistEntity
import com.gaurav.heartone.repository.SongEntity
import com.gaurav.heartone.repository.UserEntity
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationClient.getResponse
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class HomeActivity : AppCompatActivity() {

    private val httpClient = OkHttpClient()

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
                    "user-top-read",
                    "playlist-read-collaborative",
                    "user-library-read",
                    "user-read-playback-state"
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
                getUserId()
                val i = Intent(this@HomeActivity , AppActivity::class.java)
                startActivity(i)
                finish()
            }
            else{
                Toast.makeText(this@HomeActivity,"Authorization wasn't completed",Toast.LENGTH_LONG)
            }

        }
    }

    fun getUserId(){
        val sharedPreferences : SharedPreferences? = getSharedPreferences("sharedPrefs",
            Context.MODE_PRIVATE)
        val accessToken = sharedPreferences?.getString("ACCESS_TOKEN",null)
        val request = Request.Builder()
            .url("https://api.spotify.com/v1/me")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        Thread{
            httpClient.newCall(request).execute().use { response ->
                val responseString = response.body?.string()
                val responseMap = Gson().fromJson(responseString,Map::class.java)
                val User = UserEntity(
                    responseMap["id"] as String,accessToken,
                    responseMap["display_name"] as String?)
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java, "databse-name"
                ).build()
                val userDao = db.userDao()
                userDao.insertAll(User)
            }
            getPlaylists()
        }.start()
    }

    fun getPlaylists(){
        Thread{
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "databse-name"
            ).fallbackToDestructiveMigration().build()
            val playlistDao = db.playlistDao()
            val user = db.userDao().getAll()[0]
            val request = Request.Builder()
                .url("https://api.spotify.com/v1/users/${user.id}/playlists")
                .addHeader("Authorization", "Bearer ${user.token}")
                .build()
                httpClient.newCall(request).execute().use { response ->
                    val responseString = response.body?.string()
                    val responseMap = Gson().fromJson(responseString,Map::class.java)
                    val items = responseMap["items"] as List<LinkedTreeMap<String,*>>
                    for (item in items) {
                        val tempId = item["id"]
                        val tempName = item["name"]
                        val tempImageURL = item["images"] as ArrayList<Map<*,*>>

                        val trackData = item["tracks"] as LinkedTreeMap<String,*>
                        val tempTrackCount = trackData["total"]
                        val tempTracks = trackData["href"]
                        val tempPlaylist = tempId?.let { PlaylistEntity(it as String,
                            tempName as String?,
                            tempImageURL[0]["url"] as String?, tempTrackCount as Double?,
                            tempTracks as String?
                        ) }
                        if (tempPlaylist != null) {
                            playlistDao.insertAll(tempPlaylist)
                        }
                    }
                }

        }.start()

    }
}

