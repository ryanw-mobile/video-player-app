/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ryanwong.dazn.codechallenge.data.repository.DaznApiRepository

@Suppress("UNCHECKED_CAST")
class EventsViewModelFactory(
    private val daznApiRepository: DaznApiRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (EventsViewModel(daznApiRepository) as T)
}

