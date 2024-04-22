/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.viewmodel

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.rwmobi.dazncodechallenge.di.DispatcherModule
import com.rwmobi.dazncodechallenge.domain.model.Event
import com.rwmobi.dazncodechallenge.domain.repository.Repository
import com.rwmobi.dazncodechallenge.ui.destinations.events.EventsUIState
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
class EventsViewModel
@Inject
constructor(
    private val repository: Repository,
    private val imageLoader: ImageLoader,
    @DispatcherModule.MainDispatcher private val dispatcher: CoroutineDispatcher,
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

    fun refresh() {}

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

    private fun addErrorMessage(currentUiState: EventsUIState, message: String): EventsUIState {
        val newErrorMessage = ErrorMessage(
            id = UUID.randomUUID().mostSignificantBits,
            message = message,
        )
        return currentUiState.copy(
            isLoading = false,
            errorMessages = currentUiState.errorMessages + newErrorMessage,
        )
    }

    //  val showErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()

    // This is to maintain the recyclerview scrolling state during list refresh
    private var _listState: Parcelable? = null
    val listState: Parcelable?
        get() = _listState

    fun saveListState(listScrollingState: Parcelable?) {
        _listState = listScrollingState
    }

    // directly expose the list contents from the repository
    val listContents = repository.observeEvents()

    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    val showNoData: LiveData<Boolean> =
        listContents.map { list ->
            list.isEmpty()
        }

    private val _openVideoPlayerUrl = MutableLiveData<String?>(null)
    val openVideoPlayerUrl: LiveData<String?>
        get() = _openVideoPlayerUrl

    init {
        refreshList()
    }

    fun refreshList() {
        _showLoading.value = true
        viewModelScope.launch(dispatcher) {
            try {
                // Expected exceptions
                repository.refreshEvents()
            } catch (ex: Exception) {
                ex.printStackTrace()
                //  showErrorMessage.postValue(ex.message)
            }
            _showLoading.postValue(false)
        }
    }

    fun setEventClicked(event: Event) {
        event.videoUrl.let {
            _openVideoPlayerUrl.value =
                when (it.isEmpty()) {
                    false -> it
                    true -> null
                }
        }
    }

    fun notifyVideoPlayerNavigationCompleted() {
        _openVideoPlayerUrl.value = null
    }
}
