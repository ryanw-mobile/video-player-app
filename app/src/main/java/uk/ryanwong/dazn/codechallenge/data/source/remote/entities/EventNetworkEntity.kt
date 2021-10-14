package uk.ryanwong.dazn.codechallenge.data.source.remote.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import java.util.*

@JsonClass(generateAdapter = true)
data class EventNetworkEntity(

    @Json(name = "id")
    val eventId: Int,

    @Json(name = "title")
    val title: String,

    @Json(name = "subtitle")
    val subtitle: String,

    @Json(name = "date")
    val date: Date,

    @Json(name = "imageUrl")
    val imageUrl: String,

    @Json(name = "videoUrl")
    val videoUrl: String
)

fun List<EventNetworkEntity>.asDomainModel(): List<Event> {
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

fun List<Event>.asNetworkModel(): List<EventNetworkEntity> {
    return map {
        EventNetworkEntity(
            eventId = it.eventId,
            title = it.title,
            subtitle = it.subtitle,
            date = it.date,
            imageUrl = it.imageUrl,
            videoUrl = it.videoUrl
        )
    }
}