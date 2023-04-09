package com.gaurav.heartone



import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaurav.heartone.repository.PlaylistEntity



class PlaylistAdapter(private val data:List<PlaylistEntity>,private val activity: FragmentActivity): RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val PlaylistImage = itemview.findViewById<ImageView>(R.id.playlist_image)
        val PlaylistText = itemview.findViewById<TextView>(R.id.playlist_name)
        val PlaylistSongCount = itemview.findViewById<TextView>(R.id.song_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_playlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        Glide.with(activity)
            .load(item.imageURI)
            .into(holder.PlaylistImage)
        holder.PlaylistText.text = item.name
        holder.PlaylistSongCount.text = "${item.trackCount?.toInt().toString()} Tracks"
        holder.itemView.setOnClickListener {
            val id = item.id
            DataHolder.id = id
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, Song())
            fragmentTransaction.commit()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }




}
