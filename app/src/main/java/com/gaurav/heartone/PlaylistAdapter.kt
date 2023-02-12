package com.gaurav.heartone

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaylistAdapter(private val data:List<PlaylistItem>): RecyclerView.Adapter<PlaylistAdapter.ViewHolder>(){
        class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
            //        val PlaylistImage = itemview.findViewById(R.id.playlist_image)
            val PlaylistText = itemview.findViewById<TextView>(R.id.playlist_name)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
            val view = android.view.LayoutInflater.from(parent.context).inflate(R.layout.cardview_playlist , parent , false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int){
            val item = data[position]
//        holder.PlaylistImage = item.image
            holder.PlaylistText.text = item.text
        }

        override fun getItemCount(): Int{
            return data.size
        }
    }
