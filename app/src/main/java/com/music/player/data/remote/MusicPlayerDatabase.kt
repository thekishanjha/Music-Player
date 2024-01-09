package com.music.player.data.remote

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.music.player.data.model.FavouriteModel

@Database(entities = [FavouriteModel::class], version = 1, exportSchema = false)
abstract class MusicPlayerDatabase():RoomDatabase() {
    abstract fun favouriteData():FavouriteDao

    companion object {
        @Volatile
        private var INSTANCE: MusicPlayerDatabase? = null
        fun getDataBase(context: Context): MusicPlayerDatabase{
            val temp= INSTANCE
            synchronized(this) {
                var instance = INSTANCE
                if(temp !=null){
                    return temp
                }
                synchronized(this){
                    val instance= Room.databaseBuilder(
                        context.applicationContext,
                        MusicPlayerDatabase::class.java,
                        "musicplayerdb"
                    ).build()

                    INSTANCE=instance
                    return instance
                }
            }
        }
    }
}