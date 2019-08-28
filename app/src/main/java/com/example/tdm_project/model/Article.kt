package com.example.tdm_project.model

import android.content.Context
import android.os.Parcelable
import android.text.Html
import android.text.format.DateFormat
import android.text.format.DateUtils
import androidx.room.*
import com.example.tdm_project.model.data.Feed
import com.example.tdm_project.services.App
import com.rometools.rome.feed.synd.SyndEntry
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "articles",
    indices = [(Index(value = ["categoryId"])), (Index(value = ["uri"], unique = true))],
    foreignKeys = [(ForeignKey(entity = Category::class,
        parentColumns = ["_id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE))])

@Parcelize
data class Article(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    var _id: String = "",
    @ColumnInfo(name = "title")
    var title: String = "Untiteled Article",
    var resume: String? = null, //description
    var author: String = "",
    var uri: String? = null,   //link from feed
    var categoryId: String = "",
    var feedId: Long = 0L,
    var categoryOrigin : String = "Default",
    var fetchDate: Date = Date(),
    var publicationDate: Date = fetchDate,
    var img: String? = null,
    var mobilizedContent: String? = null, //in case i want to save my article
    var isRead: Boolean = false, //TODO(do i need to handle this)
    var isSavedOffline : Boolean = false //in case i wanted it to be readible offline
) : Parcelable, Comparable<Article> {
    override fun compareTo(other: Article): Int {
        return if (other._id == _id) 0
        else -1
    }

    fun getReadablePublicationDate(context: Context): String =
        if (DateUtils.isToday(publicationDate.time)) {
            DateFormat.getTimeFormat(context).format(publicationDate)
        } else {
            DateFormat.getMediumDateFormat(context).format(publicationDate) + ' ' +
                    DateFormat.getTimeFormat(context).format(publicationDate)
        }

}

fun SyndEntry.toDbFormat(feed: Feed , category: Category): Article {
    val item = Article()
    item._id = (feed.id.toString() + "_" + (link ?: uri ?: title
    ?: UUID.randomUUID().toString()))
    item.categoryId = category._id
    item.feedId = feed.id
    @Suppress("DEPRECATION")
    item.title = Html.fromHtml(title).toString()
    item.categoryOrigin = category.title
    item.resume = contents.getOrNull(0)?.value ?: description?.value
    //either we get firt parag or description value from rss
    item.uri = link
    item.author = author
    val date = publishedDate ?: updatedDate
    item.publicationDate = if (date?.before(item.publicationDate) == true) date else item.publicationDate

    return item
}