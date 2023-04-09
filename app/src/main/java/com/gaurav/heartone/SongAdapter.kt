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
        val xRange = -1..1
        val yRange = -1..1
        val quadrants = mutableMapOf<String, List<Pair<Double, Double>>>()
        quadrants["angry"] =
            listOf(Pair(xRange.first.toDouble(), yRange.last.toDouble()), Pair(0.0, 0.0))
        quadrants["sad"] =
            listOf(Pair(xRange.first.toDouble(), yRange.first.toDouble()), Pair(0.0, 0.0))
        quadrants["neutral"] =
            listOf(Pair(xRange.last.toDouble(), yRange.first.toDouble()), Pair(0.0, 0.0))
        quadrants["happy"] =
            listOf(Pair(xRange.last.toDouble(), yRange.last.toDouble()), Pair(0.0, 0.0))
        val sharedPreferences: SharedPreferences? = activity.getSharedPreferences(
            "sharedPrefs",
            Context.MODE_PRIVATE
        )
        val mood = sharedPreferences?.getString("MOOD", null)
        val corner = quadrants[mood]!![0]
        val distance = sqrt(
            (item.valence?.minus(corner.first)!!).pow(2.0) + (item.energy?.minus(
                corner.second
            )!!).pow(2.0)
        )
        return String.format("%.2f",distance / sqrt(2.0) * 100)
    }

    private fun playSong(item: SongEntity){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("spotify:track:${item.sid}")
        intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + activity.baseContext.packageName))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.baseContext.startActivity(intent)
    }


}