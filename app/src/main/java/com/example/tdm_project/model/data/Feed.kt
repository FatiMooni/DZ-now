package com.example.tdm_project.model.data

import android.os.Parcelable
import androidx.room.*
import com.example.tdm_project.model.Category
import com.rometools.rome.feed.synd.SyndFeed
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "feeds",
    indices = [(Index(value = ["categoryId"])),
        (Index(
            value = ["feedId", "feedLink"],
            unique = true
        ))],
    foreignKeys = [(ForeignKey(
        entity = Category::class,
        parentColumns = ["_id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    ))]
)
@Parcelize
data class Feed(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "feedId")
    var id: Long = 0L,
    @ColumnInfo(name = "feedLink")
    var link: String = "",
    @ColumnInfo(name = "feedTitle")
    var title: String? = null,
    @ColumnInfo(name = "categoryId")
    var categoryId: String,
    var fetchError: Boolean = false,
    @Deprecated("Not used anymore")
    var lastManualActionUid: String = ""
) : Parcelable


{
    fun update(feed: SyndFeed) {
        if (title == null) {
            title = feed.title
        }

        // no error anymore since we just got a feedWithCount
        fetchError = false
    }
}