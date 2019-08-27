package com.example.tdm_project.services.dao

import androidx.room.*
import com.example.tdm_project.model.data.Feed

@Dao
interface FeedDao {

    //retrive all feeds
    @Query("Select * from feeds")
    fun getAllFeeds(): List<Feed>

    //retrieve feeds that belongs to this category
    @Query("Select * from feeds where categoryId == :id")
    fun getCategoryFeeds(id : String):  List<Feed>


    //insert a feed
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg feeds: Feed)

    //modify a feed
    @Update
    fun update(vararg feeds: Feed)

    //delete a feed
    @Delete
    fun delete(vararg feeds: Feed)
}