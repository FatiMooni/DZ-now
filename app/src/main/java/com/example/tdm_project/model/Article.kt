package com.example.tdm_project.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.text.Html
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.DrawableUtils
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.room.*
import com.example.tdm_project.model.data.Feed
import com.rometools.rome.feed.synd.SyndEntry
import com.squareup.picasso.Picasso
import kotlinx.android.parcel.Parcelize
import java.util.*
import com.example.tdm_project.R
import com.example.tdm_project.model.data.ArticlePost
import okio.ByteString


@Entity(
    tableName = "articles",
    indices = [(Index(value = ["categoryId"])), (Index(value = ["uri"], unique = true))],
    foreignKeys = [(ForeignKey(
        entity = Category::class,
        parentColumns = ["_id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    ))]
)

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
    var categoryOrigin: String = "Default",
    var fetchDate: Date = Date(),
    var publicationDate: Date = fetchDate,
    var img: String? = null,
    var userId: String ="" ,
    @ColumnInfo(name = "mobilizedContent")
    var mobilizedContent: String = "", //in case i want to save my article
    var isRead: Boolean = false,
    var isSavedOffline: Boolean = false //in case i wanted it to be readible offline
) : Parcelable, Comparable<Article>, ViewModel() {
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
     companion object {
         @JvmStatic
         @BindingAdapter("app:imageUrl")
         fun loadImage(view: ImageView, imageUrl: String?) {
             if (imageUrl != null && imageUrl.isNotBlank() && imageUrl.isNotEmpty())
                 Picasso.get()
                     .load(imageUrl)
                     .error(R.drawable.chicanery)
                     .into(view)
             Log.i("picasso", "$imageUrl")
         }

     }
}

fun SyndEntry.toDbFormat(feed: Feed, category: Category): Article {

    //we will create our article based on 3 elements :
    // first -> the syndicte entry == near to be an article item
    // second -> the feed that we got from database
    // third -> category in order to save it later

    val item = Article()
    item._id = (feed.id.toString() + "_" + (link ?: uri ?: title
    ?: UUID.randomUUID().toString())).sh1()
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

fun ArticlePost.toDbFormat() : Article{

    var a =Article()
    a.userId= userId
    a._id= articleId
    a.author= author
    a.categoryId = categoryId
    a.publicationDate = Date(publicationDate)
    a.title = title
    a.isSavedOffline=true
    a.uri= uri
    a.categoryOrigin=categoryOrigin


    return a
}

fun String.sh1(): String = ByteString.of(*this.toByteArray()).sha1().hex()