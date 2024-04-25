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
import com.rwmobi.dazncodechallenge.ui.destinations.schedule.ScheduleUIState
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
class ScheduleViewModel @Inject constructor(
    private val repository: Repository,
    private val imageLoader: ImageLoader,
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ScheduleUIState> = MutableStateFlow(ScheduleUIState(isLoading = true))
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
            val isSuccess = getSchedule()
            if (isSuccess) {
                refresh()
            }
        }
    }

    fun refresh() {
        startLoading()

        viewModelScope.launch(dispatcher) {
            val refreshResult = repository.refreshSchedule()
            if (refreshResult.isFailure) {
                val exception = refreshResult.exceptionOrNull() ?: Exception("Unknown network communication exception")
                Timber.tag("refresh").e(exception)
                updateUIForError(exception.message ?: "Unknown network communication error")
                return@launch
            }

            getSchedule()
        }
    }

    private suspend fun getSchedule(): Boolean {
        val getScheduleResult = repository.getSchedule()
        return when (getScheduleResult.isSuccess) {
            false -> {
                updateUIForError("Error getting data: ${getScheduleResult.exceptionOrNull()?.message}")
                false
            }

            true -> {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = false,
                        schedules = getScheduleResult.getOrNull() ?: emptyList(),
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
        _uiState.update {
            addErrorMessage(
                currentUiState = it,
                message = message,
            )
        }
    }

    private fun addErrorMessage(currentUiState: ScheduleUIState, message: String): ScheduleUIState {
        val newErrorMessage = ErrorMessage(
            id = UUID.randomUUID().mostSignificantBits,
            message = message,
        )
        return currentUiState.copy(
            // So that each time we show a message we expect loading should have stopped
            isLoading = false,
            errorMessages = currentUiState.errorMessages + newErrorMessage,
        )
    }
}
