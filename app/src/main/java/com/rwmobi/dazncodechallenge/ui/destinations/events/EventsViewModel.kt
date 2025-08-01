/*
 * Copyright (c) 2025. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.destinations.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rwmobi.dazncodechallenge.di.DispatcherModule
import com.rwmobi.dazncodechallenge.domain.repository.Repository
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
    @DispatcherModule.IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState: MutableStateFlow<EventsUIState> =
        MutableStateFlow(EventsUIState(isLoading = true))
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
            refreshResult.fold(
                onSuccess = {
                    getEvents()
                },
                onFailure = { exception ->
                    Timber.Forest.tag("refresh").e(exception)
                    updateUIForError(exception.message ?: "Unknown network communication error")
                },
            )
        }
    }

    private suspend fun getEvents(setLoadingCompleted: Boolean = true): Boolean {
        val getEventsResult = repository.getEvents()
        return getEventsResult.fold(
            onSuccess = { events ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = !setLoadingCompleted,
                        events = events,
                    )
                }
                true
            },
            onFailure = { exception ->
                updateUIForError("Error getting data: ${exception.message}")
                false
            },
        )
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
