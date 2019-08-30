package com.example.tdm_project.services.dao

import androidx.paging.DataSource
import androidx.room.*
import com.example.tdm_project.model.Article
import com.example.tdm_project.model.Category
import com.example.tdm_project.viewmodel.ArticleViewModel

@Dao
interface ArticleDao {
    //retrive all articles
    //clicked when chosen category is tous i.e. all
    @Query("Select * from articles where fetchDate <= :maxDate order by publicationDate DESC ")
    fun getAllArticles(maxDate :Long): DataSource.Factory<Int,Article>

    //retrive all articles of a certain category
    @Query("Select * from articles where categoryId==:themeId AND fetchDate <= :maxDate order by publicationDate DESC ")
    fun getArticlesOfCategory(themeId : String , maxDate :Long): DataSource.Factory<Int,Article>

    //retrive all ids of articles thaat belong to  a certain category
    @Query("Select _id from articles where categoryId==:idcat and feedId ==:idFeed")
    fun getIdsOfArticles(idcat : String , idFeed : Long) : List<String>


    //delete all articles that have been fetched 24hr ago
    @Query("Delete from articles where fetchDate <= :maxDate")
    fun deleteAllArticles(maxDate: Long)

    //insert a Category
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg articleViewModel: Article)

    //modify a Category
    @Update
    fun update(vararg articleViewModel: Article)

    //delete a Category
    @Delete
    fun delete(vararg articleViewModel: Article)
}