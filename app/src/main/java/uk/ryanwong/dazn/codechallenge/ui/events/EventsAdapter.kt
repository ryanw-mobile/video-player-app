/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import uk.ryanwong.dazn.codechallenge.databinding.ListitemEventsBinding
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.ui.events.EventsAdapter.ViewHolder

class EventsAdapter(private val clickListener: EventClickListener) :
    ListAdapter<Event, ViewHolder>(EventsDiffCallback()) {
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)

        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    // This is needed for hasStableIds()
    override fun getItemId(position: Int): Long {
        // The API returns slightly timestamp for the same object every time, we can't use hashcode
        return position.toLong()
    }

    class ViewHolder private constructor(val binding: ListitemEventsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Event,
            clickListener: EventClickListener,
        ) {
            binding.event = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListitemEventsBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 *
 * Reference on Kotlin data class comparison:
 * https://medium.com/analytics-vidhya/data-class-in-kotlin-32bf038729a1
 */
class EventsDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(
        oldItem: Event,
        newItem: Event,
    ): Boolean {
        Timber.v("areItemsTheSame: ${oldItem.eventId == newItem.eventId}")
        return oldItem.eventId == newItem.eventId
    }

    override fun areContentsTheSame(
        oldItem: Event,
        newItem: Event,
    ): Boolean {
        Timber.v("areContentsTheSame: ${oldItem == newItem}")
        return oldItem == newItem
    }
}

class EventClickListener(val clickListener: (event: Event) -> Unit) {
    fun onClick(event: Event) = clickListener(event)
}
