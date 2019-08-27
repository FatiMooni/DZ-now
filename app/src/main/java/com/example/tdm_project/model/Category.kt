package com.example.tdm_project.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "categories")

data class Category(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val _id: String,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "feedsList")
    var feeds: Map<String, String>
) : Parcelable