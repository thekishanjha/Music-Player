package com.music.player.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.music.player.R
import com.music.player.data.model.FavouriteModel

import com.music.player.presentation.musicPlayer.MusicPlayer
import java.util.Locale

class MusicAdapter(private val musicTracks: List<FavouriteModel>) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    private var filteredMusicList: List<FavouriteModel> = musicTracks
    private lateinit var mContext:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.music_list_items, parent, false)
        mContext=parent.context
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = filteredMusicList[position]
        holder.titleTextView.text = track.title
        val minutes=track.duration/1000/60
        val seconds=(track.duration/1000)%60
        val currentTime=String.format("%02d:%02d",minutes,seconds)
        holder.duration.text=currentTime
        holder.musicArtist.text=track.artist
        //navigate to musicplayer page on clicking of any item from list
        holder.itemView.setOnClickListener {
            val  intent=Intent(mContext,MusicPlayer::class.java)
            intent.putExtra("Music_list",track)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredMusicList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.music_title)
        val musicArtist: TextView = itemView.findViewById(R.id.music_artist)
        val duration: TextView = itemView.findViewById(R.id.music_duration)
    }

    //searching the item from list
    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        filteredMusicList = if (query.isBlank()) {
            musicTracks
        } else {
            musicTracks.filter { musicItem ->
                musicItem.title.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        musicItem.artist.lowercase(Locale.getDefault())
                            .contains(lowerCaseQuery) ||
                        musicItem.album.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
            }
        }
        notifyDataSetChanged()
    }
    //formatting millisecond into Minute:Second format
    fun formatMilliseconds(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "$minutes:$seconds"
    }

}