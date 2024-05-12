/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.rwmobi.dazncodechallenge.di.DispatcherModule
import com.rwmobi.dazncodechallenge.domain.repository.Repository
import com.rwmobi.dazncodechallenge.ui.destinations.events.EventsUIState
import com.rwmobi.dazncodechallenge.ui.model.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: Repository,
    private val imageLoader: ImageLoader,
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState: MutableStateFlow<EventsUIState> = MutableStateFlow(EventsUIState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    fun errorShown(errorId: Long) {
        _uiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    fun requestScrollToTop(enabled: Boolean) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                requestScrollToTop = enabled,
            )
        }
    }

    fun getImageLoader() = imageLoader

    fun fetchCacheAndRefresh() {
        startLoading()
        viewModelScope.launch(dispatcher) {
            val isSuccess = getEvents(setLoadingCompleted = false)
            if (isSuccess) {
                refresh()
            }
        }
    }

    fun refresh() {
        startLoading()

        viewModelScope.launch(dispatcher) {
            val refreshResult = repository.refreshEvents()
            if (refreshResult.isFailure) {
                val exception = refreshResult.exceptionOrNull() ?: Exception("Unknown network communication exception")
                Timber.tag("refresh").e(exception)
                updateUIForError(exception.message ?: "Unknown network communication error")
            } else {
                getEvents()
            }
        }
    }

    private suspend fun getEvents(setLoadingCompleted: Boolean = true): Boolean {
        val getEventsResult = repository.getEvents()
        return when (getEventsResult.isSuccess) {
            false -> {
                updateUIForError("Error getting data: ${getEventsResult.exceptionOrNull()?.message}")
                false
            }

            true -> {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = !setLoadingCompleted,
                        events = getEventsResult.getOrNull() ?: emptyList(),
                    )
                }
                true
            }
        }
    }

    private fun startLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun updateUIForError(message: String) {
        _uiState.update { currentUiState ->
            val newErrorMessages = if (_uiState.value.errorMessages.any { it.message == message }) {
                currentUiState.errorMessages
            } else {
                currentUiState.errorMessages + ErrorMessage(
                    id = UUID.randomUUID().mostSignificantBits,
                    message = message,
                )
            }
            currentUiState.copy(
                isLoading = false,
                errorMessages = newErrorMessages,
            )
        }
    }
}
