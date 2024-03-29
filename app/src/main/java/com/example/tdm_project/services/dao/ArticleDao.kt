package com.example.tdm_project.services.dao

import androidx.paging.DataSource
import androidx.room.*
import com.example.tdm_project.model.Article

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


    //delete all articles that have been fetched 24hr ago and that are not saved offline
    @Query("Delete from articles where fetchDate <= :maxDate and isSavedOffline = 0")
    fun deleteAllArticles(maxDate: Long)

    //get the articles that have been saved offline
    @Query("Select * from articles where isSavedOffline = 1 and userId=:userId order by publicationDate DESC ")
    fun getSavedArticles(userId: String): DataSource.Factory<Int,Article>

    //update to saved
    @Query("Update articles set isSavedOffline = 1 , userId=:userId where _id is :articleId  ")
    fun markArticleAsSaved(userId : String ,articleId : String)

    //update to unsaved
    @Query("Update articles set isSavedOffline = 0 where _id is :articleId ")
    fun markArticleAsUnsaved(articleId : String)

    //select for notification
    @Query("Select * from articles where isFav = 1 order by publicationDate DESC")
    fun getFavArticles() : List<Article>

    //update to unsaved
    @SkipQueryVerification
    @Query("Update articles set mobilizedContent = :content where _id is :articleId ")
    fun markArticleOffline(articleId : String , content : String)


    //select articles with favorite category
    @Query("Select * from articles where categoryOrigin is :categoryPref ")
    fun getFavoriteArticles(categoryPref : String) : List<Article>

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