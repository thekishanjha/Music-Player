package com.music.player.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.music.player.common.getAudioTracks
import com.music.player.data.model.FavouriteModel
import com.music.player.data.remote.FavouriteDao


/* Class For all logical operation please add all operation here*/
class MainRepository (private val dao: FavouriteDao) {
    fun insertFavourite(favouriteModel: FavouriteModel) {
        dao.insertFavorite(favouriteModel)
    }

    fun deleteFavourite(favouriteModel: FavouriteModel) {
        dao.deleteFavorite(favouriteModel)
    }

    fun updateFavourite(favouriteModel: FavouriteModel) {
        dao.updateFavorite(favouriteModel)
    }
    fun getFavourite(): List<FavouriteModel> {
        return dao.getFavourite()
    }

    fun getMusicTrack(context: Context): List<FavouriteModel> {
        return getAudioTracks(context)
    }
    fun getData(): LiveData<List<FavouriteModel>> {
        return dao.getData()
    }


}