/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.dazncodechallenge.ui.viewmodel

import android.os.CountDownTimer
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
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
class ScheduleViewModel
@Inject
constructor(
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

    //  val showErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()

    // This is to maintain the recyclerview scrolling state during list refresh
    private var _listState: Parcelable? = null
    val listState: Parcelable?
        get() = _listState

    fun saveListState(listScrollingState: Parcelable?) {
        _listState = listScrollingState
    }

    // directly expose the list contents from the repository
    val listContents = repository.observeSchedule()

    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    val showNoData: LiveData<Boolean> =
        listContents.map { list ->
            list.isEmpty()
        }

    private val timer: CountDownTimer =
        object : CountDownTimer(COUNTDOWN_TIME, COUNTDOWN_TIME) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                Timber.d("Timer triggered")
                refreshList()
                autoRefresh()
            }
        }

    init {
        refreshList()
        autoRefresh()
    }

    // Needs to cancel the timer when ViewModel is destroyed to prevent memory leak
    override fun onCleared() {
        timer.cancel()
        Timber.d("Timer cancelled")
        super.onCleared()
    }

    fun refreshList() {
        _showLoading.value = true
        viewModelScope.launch(dispatcher) {
            try {
                // Expected exceptions
                repository.refreshSchedule()
            } catch (ex: Exception) {
                ex.printStackTrace()
                //       showErrorMessage.postValue(ex.message)
            }

            _showLoading.postValue(false)
        }
    }

    /**
     * Repeat the timer automatically
     * It cannot be done within CountDownTimer's onFinish() as compiler said timer is not initialized
     *
     * This approach is for meeting the requirement to run refresh every 30 seconds.
     * If we want to do background refresh even when the App is not running,
     * or not exactly every 30 seconds, we could consider migrating to use Android WorkManager.
     * WorkManager fits the purpose when the refresh:
     *   1. Does not need to run at a specific time
     *   2. Can be deferred to be executed
     *   3. Is guaranteed to run even after the app is killed or device is restarted
     *   4. Has to meet constraints like battery supply or network availability before execution
     *   Reference: https://flexiple.com/android/android-workmanager-tutorial-getting-started/
     */
    private fun autoRefresh() {
        timer.start()
    }

    companion object {
        // This is the auto refresh interval
        const val COUNTDOWN_TIME = 30000L
    }
}
