package com.example.tdm_project.services.dao

import androidx.room.*
import com.example.tdm_project.model.data.Video

@Dao
interface VideoDao {

    //retrive all videos
    @Query("Select * from videos")
    fun getVideos(): List<Video>
    //insert a video
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg video: Video)





}