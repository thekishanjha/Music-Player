package com.music.player.domain.use_case.welcomeMusicList

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.music.player.data.model.FavouriteModel
import com.music.player.data.remote.MusicPlayerDatabase
import com.music.player.data.repository.MainRepository

// ViewModel for getting Music List from mobile and  Connecting UseCase and Activity of Welcome Page

class WelcomePageViewModel (application: Application): AndroidViewModel(application) {
    private lateinit var mainRepo: MainRepository
    init {
        val db= MusicPlayerDatabase.getDataBase(application.applicationContext)
        val dao=db.favouriteData()
        mainRepo= MainRepository(dao)

    }

    fun getAudioTrack(context: Context): List<FavouriteModel> {
        return mainRepo.getMusicTrack(context)
    }

}