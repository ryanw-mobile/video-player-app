package uk.ryanwong.dazn.codechallenge.data.source.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ryanwong.dazn.codechallenge.domain.models.Schedule
import java.util.Date

@Entity(tableName = "schedule_table")
data class ScheduleDbEntity(

    @ColumnInfo(name = "schedule_id")
    @PrimaryKey
    val scheduleId: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "subtitle")
    val subtitle: String,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    // This field is not from the RestAPI
    // Every time we overwrite the DB with API data. To remove outdated data in the FB,
    // we use this extra dirty bit approach to simulate synchronization.
    @ColumnInfo(name = "dirty")
    val dirty: Boolean = false
)

fun List<ScheduleDbEntity>.asDomainModel(): List<Schedule> {
    return map {
        it.asDomainModel()
    }
}

fun ScheduleDbEntity.asDomainModel(): Schedule {
    return Schedule(
        scheduleId = this.scheduleId,
        title = this.title,
        subtitle = this.subtitle,
        date = this.date,
        imageUrl = this.imageUrl
    )
}

fun List<Schedule>.asDatabaseModel(): List<ScheduleDbEntity> {
    return map {
        it.asDatabaseModel()
    }
}

fun Schedule.asDatabaseModel(): ScheduleDbEntity {
    return ScheduleDbEntity(
        scheduleId = this.scheduleId,
        title = this.title,
        subtitle = this.subtitle,
        date = this.date,
        imageUrl = this.imageUrl,
        dirty = false
    )
}
