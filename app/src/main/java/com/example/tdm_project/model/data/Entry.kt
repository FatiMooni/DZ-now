package com.example.tdm_project.model.data

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.Html
import android.text.format.DateFormat
import android.text.format.DateUtils
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.tdm_project.model.Category
import java.util.*
import com.rometools.rome.feed.synd.SyndEntry
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "entries",
    indices = [(Index(value = ["categoryId"])), (Index(value = ["link"], unique = true))],
    foreignKeys = [(ForeignKey(entity = Category::class,
        parentColumns = ["_id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE))])

data class Entry(@PrimaryKey
                 var id: String = "",
                 var categoryId: Long = 0L,
                 var categoryOrigin : String = "Default",
                 var link: String? = null,
                 var fetchDate: Date = Date(),
                 var publicationDate: Date = fetchDate, // important to know if the publication date has been set
                 var title: String? = null,
                 var description: String? = null,
                 var mobilizedContent: String? = null,
                 var imageLink: String? = null,
                 var author: String? = null,
                 var read: Boolean = false,
                 var favorite: Boolean = false) : Parcelable {

    fun getReadablePublicationDate(context: Context): String =
        if (DateUtils.isToday(publicationDate.time)) {
            DateFormat.getTimeFormat(context).format(publicationDate)
        } else {
            DateFormat.getMediumDateFormat(context).format(publicationDate) + ' ' +
                    DateFormat.getTimeFormat(context).format(publicationDate)
        }

}

fun SyndEntry.toDbFormat(feed: Feed): Entry {
    val item = Entry()
    item.id = (feed.id.toString() + "_" + (link ?: uri ?: title
    ?: UUID.randomUUID().toString()))
    item.categoryId = feed.id
    @Suppress("DEPRECATION")
    item.title = Html.fromHtml(title).toString()
    item.categoryOrigin = categories[0].toString()
    item.description = contents.getOrNull(0)?.value ?: description?.value
    item.link = link
    item.author = author
    val date = publishedDate ?: updatedDate
    item.publicationDate = if (date?.before(item.publicationDate) == true) date else item.publicationDate

    return item
}