package com.example.tdm_project.model.data
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import okio.ByteString

@Parcelize
@Entity(tableName = "videos")

data class Video (
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id : Long = 0L,
    @ColumnInfo(name = "_title")
    var videoTitle : String,
    @ColumnInfo(name = "_uri")
    var videoUri : String ,
    @ColumnInfo(name = "_thumb")
    var thumbnail : String


): Parcelable
{
    /*constructor():this(null,
        "","","")*/
}

