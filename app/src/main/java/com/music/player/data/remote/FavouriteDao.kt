package com.music.player.data.remote

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.music.player.data.model.FavouriteModel

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favouriteModel: FavouriteModel)

    @Update
    fun updateFavorite(favouriteModel: FavouriteModel)

    @Delete
    fun deleteFavorite(favouriteModel: FavouriteModel)

    @Query("SELECT * FROM favouriteSongs ")
    fun getFavourite():List<FavouriteModel>

    @Query("SELECT * FROM favouriteSongs")
    fun getData():LiveData<List<FavouriteModel>>

}