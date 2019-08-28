package com.example.tdm_project.services.dao

import androidx.room.*
import com.example.tdm_project.model.Category

@Dao
interface CategoryDao {

    //retrive all categories
    @Query("Select * from categories")
    fun getAllCategories(): List<Category>


    //retrive all categories
    @Query("Select * from categories where _id==:id")
    fun getCategory(id :String): Category

    //insert a Category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg category: Category)

    //modify a Category
    @Update
    fun update(vararg category: Category)

    //delete a Category
    @Delete
    fun delete(vararg category: Category)
}