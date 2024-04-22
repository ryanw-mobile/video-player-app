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

    fun refresh() {
        startLoading()

        viewModelScope.launch(dispatcher) {
            val refreshResult = repository.refreshSchedule()
            if (refreshResult.isFailure) {
                val exception = refreshResult.exceptionOrNull() ?: Exception("Unknown exception")
                exception.printStackTrace()
                updateUIForError(exception.message ?: "some error")
                stopLoading()
                return@launch
            }

            val getScheduleResult = repository.getSchedule()
            when (getScheduleResult.isFailure) {
                true -> {
                    updateUIForError("Error getting data: ${getScheduleResult.exceptionOrNull()?.message}")
                    stopLoading()
                }

                false -> _uiState.update { currentUiState ->
                    currentUiState.copy(
                        isLoading = false,
                        schedules = getScheduleResult.getOrNull() ?: emptyList(),
                    )
                }
            }
        }
    }

    private fun startLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun stopLoading() {
        _uiState.update { it.copy(isLoading = false) }
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
            isLoading = false,
            errorMessages = currentUiState.errorMessages + newErrorMessage,
        )
    }
}
