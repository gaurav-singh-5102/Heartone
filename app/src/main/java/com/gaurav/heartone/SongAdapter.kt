package com.gaurav.heartone


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaurav.heartone.repository.SongEntity
import kotlin.math.pow
import kotlin.math.sqrt


class SongAdapter(private val data:List<SongEntity>,private val activity: FragmentActivity): RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val SongImage = itemview.findViewById<ImageView>(R.id.song_image)
        val SongName = itemview.findViewById<TextView>(R.id.song_name)
        val Confidence = itemview.findViewById<TextView>(R.id.heart_handshake_pct)
        val Player = itemview.findViewById<ImageView>(R.id.play_song)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        Glide.with(activity)
            .load(item.image)
            .into(holder.SongImage)
        holder.SongName.text = item.name
        holder.Confidence.text = getPercentage(item)
        holder.Player.setOnClickListener{
            playSong(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun getPercentage(item: SongEntity): String {
        val regions = mutableMapOf<String, Pair<Float,Float>>()
        regions["angry"] = Pair(0.5f,-0.5f)
        regions["sad"] = Pair(-0.5f,-0.5f)
        regions["neutral"] = Pair(-0.5f,0.5f)
        regions["happy"] =Pair(0.5f,0.5f)
        val sharedPreferences: SharedPreferences? = activity.getSharedPreferences(
            "sharedPrefs",
            Context.MODE_PRIVATE
        )
        var songPoint = Pair(item.valence.toFloat(),item.energy.toFloat())
        val mood = sharedPreferences?.getString("MOOD", null)
        val distance = regions[mood]?.let { calculateDistance(songPoint, it) }
        return String.format("%.2f",(1 - distance!!) * 100)
    }

    private fun calculateDistance(pointA : Pair<Float,Float>, pointB: Pair<Float,Float>):Float{
        return sqrt((pointA.first - pointB.first).pow(2)+ (pointA.second - pointB.second).pow(2))
    }

    private fun playSong(item: SongEntity){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("spotify:track:${item.sid}")
        intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + activity.baseContext.packageName))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.baseContext.startActivity(intent)
    }


}