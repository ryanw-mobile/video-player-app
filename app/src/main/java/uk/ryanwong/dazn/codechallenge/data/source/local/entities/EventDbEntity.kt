package uk.ryanwong.dazn.codechallenge.data.source.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import java.util.*

@Entity(tableName = "event_table")
data class EventDbEntity(

    @ColumnInfo(name = "event_id")
    @PrimaryKey
    val eventId: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "subtitle")
    val subtitle: String,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "video_url")
    val videoUrl: String,

    // This field is not from the RestAPI
    // Every time we overwrite the DB with API data. To remove outdated data in the FB,
    // we use this extra dirty bit approach to simulate synchronization.
    @ColumnInfo(name = "dirty")
    val dirty: Boolean = false
)

fun List<EventDbEntity>.asDomainModel(): List<Event> {
    return map {
        Event(
            eventId = it.eventId,
            title = it.title,
            subtitle = it.subtitle,
            date = it.date,
            imageUrl = it.imageUrl,
            videoUrl = it.videoUrl
        )
    }
}

fun EventDbEntity.asDomainModel(): Event {
    return Event(
        eventId = this.eventId,
        title = this.title,
        subtitle = this.subtitle,
        date = this.date,
        imageUrl = this.imageUrl,
        videoUrl = this.videoUrl
    )
}

fun List<Event>.asDatabaseModel(): List<EventDbEntity> {
    return map {
        EventDbEntity(
            eventId = it.eventId,
            title = it.title,
            subtitle = it.subtitle,
            date = it.date,
            imageUrl = it.imageUrl,
            videoUrl = it.videoUrl,
            dirty = false
        )
    }
}

fun Event.asDatabaseModel(): EventDbEntity {
    return EventDbEntity(
        eventId = this.eventId,
        title = this.title,
        subtitle = this.subtitle,
        date = this.date,
        imageUrl = this.imageUrl,
        videoUrl = this.videoUrl,
        dirty = false
    )
}