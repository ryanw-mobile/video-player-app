package uk.ryanwong.dazn.codechallenge.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.repository.DaznApiRepository
import uk.ryanwong.dazn.codechallenge.util.SingleLiveEvent

class EventsViewModel(private val daznApiRepository: DaznApiRepository) : ViewModel() {

    val showLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val showErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()
    val showNoData: MutableLiveData<Boolean> = MutableLiveData()
    val openVideoPlayerUrl: SingleLiveEvent<String> = SingleLiveEvent()

    private val _eventList = MutableLiveData<List<Event>>()
    val eventList: LiveData<List<Event>>
        get() = _eventList

    init {
        refreshEvents()
    }

    fun refreshEvents() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                // Expected exceptions
                daznApiRepository.refreshEvents()
            } catch (ex: Exception) {
                ex.printStackTrace()
                showErrorMessage.postValue(ex.toString())
            }

            // Even the previous sync might fail, we still try to fetch whatever we have locally
            when (val apiResult = daznApiRepository.getEvents()) {
                is ApiResult.Success<List<Event>> -> {
                    _eventList.value =
                        apiResult.data!!   // !! is redundant but added to avoid IDE error
                    Timber.d("refreshEvents - fetched ${apiResult.data.size} items to live data")
                }
                is ApiResult.Error -> showErrorMessage.postValue(apiResult.exception.toString())
            }

            //check if no data has to be shown
            invalidateShowNoData()
        }
    }

    fun setEventClicked(event: Event) {
        event.videoUrl?.let {
            openVideoPlayerUrl.value = it
        }
    }

    fun notifyVideoPlayerNavigationCompleted() {
        openVideoPlayerUrl.value = null
    }

    /**
     * Inform the user that there's not any data if the list is empty
     */
    private fun invalidateShowNoData() {
        showLoading.postValue(false)
        showNoData.value = _eventList.value == null || _eventList.value!!.isEmpty()
        Timber.d("invalidateShowNoData - no date = {$showNoData.value}")
    }
}