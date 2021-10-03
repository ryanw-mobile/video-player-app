/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ryanwong.dazn.codechallenge.base.BaseRepository

@Suppress("UNCHECKED_CAST")
class EventsViewModelFactory(
    private val baseRepository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (EventsViewModel(baseRepository) as T)
}

