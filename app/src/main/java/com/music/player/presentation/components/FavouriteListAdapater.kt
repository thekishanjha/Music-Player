package com.music.player.presentation.components

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

class FavouriteListAdapater(private val list: List<FavouriteModel>) : RecyclerView.Adapter< FavouriteListAdapater.FavouriteListViewHolder>() {
    private lateinit var mContext: Context
    private var filteredMusicList: List<FavouriteModel> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteListViewHolder {
        mContext=parent.context
        return FavouriteListViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.play_list_tiem, parent, false))
    }

    override fun getItemCount(): Int {
        return filteredMusicList.size
    }

    override fun onBindViewHolder(holder: FavouriteListViewHolder, position: Int) {
        val musicListName=filteredMusicList[position]
        holder.musicTitleName.text=musicListName.title
        holder.musicArtist.text=musicListName.artist
        //navigate to musicplayer page on clicking of any item from list
        holder.itemView.setOnClickListener {
            val intent= Intent(mContext, MusicPlayer::class.java)
            intent.putExtra("Music_list",musicListName)
            mContext.startActivity(intent)
        }

    }

    class FavouriteListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val musicTitleName =view.findViewById<TextView>(R.id.playlist)
        val musicArtist=view.findViewById<TextView>(R.id.artist)

    }
}