package com.example.tdm_project.model

import android.os.Parcel
import android.os.Parcelable

data class Article (val _id : String,
                    val title: String,
                    val resume : String?,
                    val theme : String,
                    val author : String,
                    val date : String,
                    val uri : String,
                    val img: String) : Parcelable,Comparable<Article> {
    override fun compareTo(other: Article): Int {
        return if(other._id ==  _id) 0
        else -1
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(title)
        parcel.writeString(resume)
        parcel.writeString(theme)
        parcel.writeString(author)
        parcel.writeString(date)
        parcel.writeString(uri)
        parcel.writeString(img)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}