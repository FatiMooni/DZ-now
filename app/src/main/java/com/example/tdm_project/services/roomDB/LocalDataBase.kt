package com.example.tdm_project.services.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.Category
import com.example.tdm_project.model.data.Feed
import com.example.tdm_project.services.converters.Converters
import com.example.tdm_project.services.dao.ArticleDao
import com.example.tdm_project.services.dao.CategoryDao
import com.example.tdm_project.services.dao.FeedDao

@TypeConverters(Converters::class)
@Database(entities = [Feed::class, Article::class, Category::class], version = 1)
abstract class LocalDataBase : RoomDatabase() {


    companion object {
        private var INSTANCE: LocalDataBase? = null

        private val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.run {
                    execSQL("DROP TABLE articles")
                }
            }
        }
        fun getInstance(context: Context): LocalDataBase? {
            if (INSTANCE == null) {
                synchronized(LocalDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        LocalDataBase::class.java, "dznow.db"
                    )
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
    abstract fun articleDao(): ArticleDao
    abstract fun categoryDao(): CategoryDao
}