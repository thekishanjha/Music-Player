package com.music.player.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favouriteSongs")
data class FavouriteModel(
    @PrimaryKey(autoGenerate = true)
    val Id :Long,
    val title: String,
    val artist:String,
    val album:String,
    val duration: Long,
    val filepath:String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(Id)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(album)
        parcel.writeLong(duration)
        parcel.writeString(filepath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavouriteModel> {
        override fun createFromParcel(parcel: Parcel): FavouriteModel {
            return FavouriteModel(parcel)
        }

        override fun newArray(size: Int): Array<FavouriteModel?> {
            return arrayOfNulls(size)
        }
    }
}