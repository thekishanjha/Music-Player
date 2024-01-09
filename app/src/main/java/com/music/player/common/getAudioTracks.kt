package com.music.player.common

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import com.music.player.data.model.FavouriteModel

@SuppressLint("Range")
fun getAudioTracks(context: Context): List<FavouriteModel> {
    val musicTracks = mutableListOf<FavouriteModel>()

    val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATA
    )
    val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

    val cursor = context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        null,
        sortOrder
    )

    cursor?.use {
        while (it.moveToNext()) {
            val id = it.getLong(it.getColumnIndex(MediaStore.Audio.Media._ID))
            val title = it.getString(it.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artist = it.getString(it.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val album = it.getString(it.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            val duration = it.getLong(it.getColumnIndex(MediaStore.Audio.Media.DURATION))
            val filePath = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DATA))

            musicTracks.add(
                FavouriteModel(id, title, artist, album, duration, filePath)
            )
        }
    }

    return musicTracks
}