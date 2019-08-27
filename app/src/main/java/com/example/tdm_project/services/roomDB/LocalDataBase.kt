package com.example.tdm_project.services.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tdm_project.model.Category
import com.example.tdm_project.model.data.Entry
import com.example.tdm_project.model.data.Feed
import com.example.tdm_project.services.dao.CategoryDao
import com.example.tdm_project.services.dao.FeedDao
import com.example.tdm_project.services.converters.Converters

@TypeConverters(Converters::class)
@Database(entities = [Feed::class, Entry::class, Category::class], version = 3)
abstract class LocalDataBase : RoomDatabase(){


    companion object {
        private var INSTANCE: LocalDataBase? = null

        fun getInstance(context: Context): LocalDataBase? {
            if (INSTANCE == null) {
                synchronized(LocalDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        LocalDataBase::class.java, "weather.db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }


    abstract fun feedDao(): FeedDao
    //abstract fun entryDao(): EntryDao
    abstract fun categoryDao(): CategoryDao
}