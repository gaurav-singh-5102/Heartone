package com.gaurav.heartone

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.gaurav.heartone.repository.AppDatabase
import com.gaurav.heartone.repository.PlaylistEntity



class Playlist : Fragment(R.layout.fragment_playlist){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onStart() {
        super.onStart()
        val data : List<PlaylistEntity>
        val db = context?.let { Room.databaseBuilder(it,AppDatabase::class.java,"databse-name").allowMainThreadQueries().fallbackToDestructiveMigration().build() }
        val playlistDao = db?.playlistDao()
        data = playlistDao?.getAll()!!
        val recycler = view?.findViewById<RecyclerView>(R.id.recycler_playlist)
        val adapter = activity?.let { PlaylistAdapter(data , it) }
        recycler?.adapter = adapter
        recycler?.layoutManager = LinearLayoutManager(context)

    }

}