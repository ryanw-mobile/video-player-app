package uk.ryanwong.dazn.codechallenge.domain.models

import java.util.Date

data class Event(
    val eventId: Int,
    val title: String,
    val subtitle: String,
    val date: Date,
    val imageUrl: String,
    val videoUrl: String
)
