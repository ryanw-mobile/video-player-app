/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import uk.ryanwong.dazn.codechallenge.databinding.ListitemScheduleBinding
import uk.ryanwong.dazn.codechallenge.ui.schedule.ScheduleAdapter.ViewHolder

class ScheduleAdapter : ListAdapter<Schedule, ViewHolder>(ScheduleDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListitemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Schedule) {
            binding.schedule = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListitemScheduleBinding.inflate(layoutInflater, parent, false)

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
 */
class ScheduleDiffCallback : DiffUtil.ItemCallback<Schedule>() {
    override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        Timber.v("areItemsTheSame: ${oldItem == newItem}")
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        Timber.v("areContentsTheSame: ${oldItem.id} vs ${newItem.id}")
        return oldItem.id == newItem.id
    }
}