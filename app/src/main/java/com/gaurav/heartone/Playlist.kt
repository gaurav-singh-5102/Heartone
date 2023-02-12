package com.gaurav.heartone

import android.content.ClipData.Item
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Playlist.newInstance] factory method to
 * create an instance of this fragment.
 */
class Playlist : Fragment(R.layout.fragment_playlist) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onStart() {
        super.onStart()
        val data = listOf<PlaylistItem>(PlaylistItem("Item1"),
            PlaylistItem("Item2"), PlaylistItem
        ("item3")
        )
        val recycler = view?.findViewById<RecyclerView>(R.id.recycler_playlist)
        val adapter = PlaylistAdapter(data)
        recycler?.adapter = adapter
        recycler?.layoutManager = LinearLayoutManager(context)

    }

}