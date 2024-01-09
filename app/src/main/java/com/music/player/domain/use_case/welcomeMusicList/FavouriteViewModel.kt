package com.music.player.domain.use_case.welcomeMusicList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.music.player.data.model.FavouriteModel
import com.music.player.data.remote.MusicPlayerDatabase
import com.music.player.data.repository.MainRepository

class FavouriteViewModel(application: Application):AndroidViewModel(application) {
    private lateinit var repository: MainRepository
    private lateinit var isFavourite:LiveData<Boolean>

    init {
        val database=MusicPlayerDatabase.getDataBase(application.applicationContext)
        val dao=database.favouriteData()
        repository= MainRepository(dao)

    }

    fun insert(favouriteModel: FavouriteModel){
        repository.insertFavourite(favouriteModel)
    }
    fun delete(favouriteModel: FavouriteModel){
        repository.deleteFavourite(favouriteModel)
    }
    fun getData(): List<FavouriteModel> {
       return repository.getFavourite()
    }

    fun getFavourite(): LiveData<List<FavouriteModel>> {
        return repository.getData()
    }



}