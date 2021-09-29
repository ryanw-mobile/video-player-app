package uk.ryanwong.dazn.codechallenge.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Transient
import java.util.*

/**
 * This data class is shared by Moshi and Room as the schema is pretty straightforward.
 */
@Entity(tableName = "schedule_table")
@JsonClass(generateAdapter = true)
data class Schedule(

    @Json(name = "id")
    @PrimaryKey
    val id: Int,

    @Json(name = "title")
    @ColumnInfo(name = "title")
    val title: String,

    @Json(name = "subtitle")
    @ColumnInfo(name = "subtitle")
    val subtitle: String,

    @Json(name = "date")
    @ColumnInfo(name = "date")
    val date: Date,

    @Json(name = "imageUrl")
    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    // This field is not from the RestAPI
    // Every time we overwrite the DB with API data. To remove outdated data in the FB,
    // we use this extra dirty bit approach to simulate synchronization.
    @Transient
    @ColumnInfo(name = "dirty")
    val dirty: Boolean = false
)