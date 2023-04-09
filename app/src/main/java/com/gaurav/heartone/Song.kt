package com.gaurav.heartone


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.gaurav.heartone.repository.AppDatabase
import com.gaurav.heartone.repository.SongEntity
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import okhttp3.OkHttpClient
import okhttp3.Request


class Song : Fragment(R.layout.fragment_song){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song, container, false)
    }

    override fun onStart() {
        super.onStart()
        val adapter : SongAdapter = activity?.let { SongAdapter( getSongs(DataHolder.id), it) }!!
        val recycler = view?.findViewById<RecyclerView>(R.id.recycler_song)
        recycler?.adapter = adapter
        recycler?.layoutManager = LinearLayoutManager(context)
    }
    private fun getSongs(pid : String): List<SongEntity> {
        val httpClient = OkHttpClient()
        val data : MutableList<SongEntity> = emptyList<SongEntity>().toMutableList()
        val t = Thread{
            val db = context?.let {
                Room.databaseBuilder(
                    it,
                    AppDatabase::class.java, "databse-name"
                ).fallbackToDestructiveMigration().build()
            }
            val user = db?.userDao()?.getAll()?.get(0)
            val accessToken = user?.token

            val request = Request.Builder()
                .url("https://api.spotify.com/v1/playlists/$pid/tracks")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            httpClient.newCall(request).execute().use { response ->
                val responseBody = Gson().fromJson(response.body?.string(),Map::class.java)
                val items = responseBody["items"] as List<LinkedTreeMap<String, *>>
                for (item in items){
                    val energy : Double
                    val valence : Double
                    val track = item["track"] as Map<*,*>
                    val id = track["id"]
                    val name = track["name"]
                    val album = track["album"] as Map<*,*>
                    val images = album["images"] as ArrayList<Map<*,*>>
                    var imageURL : String?
                    try {
                        imageURL = images[0]["url"] as String?
                    }catch (e : java.lang.IndexOutOfBoundsException){
                        imageURL = null
                    }
                    val r = Request.Builder()
                        .url("https://api.spotify.com/v1/audio-features/$id")
                        .addHeader("Authorization","Bearer $accessToken")
                        .build()
                    httpClient.newCall(r).execute().use { response ->
                        val rbody = Gson().fromJson(response.body?.string(),Map::class.java)
                        if (rbody["energy"]!=null && rbody["valence"]!=null){
                            energy = rbody["energy"] as Double
                            valence = rbody["valence"] as Double
                        }else{
                            energy =0.0
                            valence = 0.0
                        }
                    }
                    (name as String?)?.let {
                        SongEntity(
                            0,
                            id as String,
                            pid,
                            imageURL , it,energy,valence)
                    }?.let {
                        db?.runInTransaction {
                            db.songDao().insertAll(it)
                            data += it
                        }
                    }
                }
            }
        }
        t.start()
        t.join()
        return data
    }
}