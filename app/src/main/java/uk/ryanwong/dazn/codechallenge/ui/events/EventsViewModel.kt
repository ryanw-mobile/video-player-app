package uk.ryanwong.dazn.codechallenge.ui.events

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import uk.ryanwong.dazn.codechallenge.base.BaseViewModel
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.model.Event
import uk.ryanwong.dazn.codechallenge.data.repository.DaznApiRepository
import uk.ryanwong.dazn.codechallenge.util.SingleLiveEvent


class EventsViewModel(private val daznApiRepository: DaznApiRepository) : BaseViewModel() {

    val openVideoPlayerUrl: SingleLiveEvent<String> = SingleLiveEvent()

    init {
        // Quietly load the cached contents from local DB before doing a refresh
        // Errors and no data handling can be ignored because refreshList() will take care of them
        viewModelScope.launch {
            when (val apiResult = daznApiRepository.getEvents()) {
                is ApiResult.Success<List<Event>> -> {
                    _listContents.value = apiResult.data
                }
            }
        }
        refreshList()
    }

    override fun refreshList() {
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
                    _listContents.value = apiResult.data
                    Timber.d("refreshEvents - fetched ${apiResult.data.size} items to live data")
                }
                is ApiResult.Error -> showErrorMessage.postValue(apiResult.exception.toString())
            }

            //check if no data has to be shown
            invalidateShowNoData()
        }
    }

    fun setEventClicked(event: Event) {
        event.videoUrl.let {
            openVideoPlayerUrl.value = when (it.isEmpty()) {
                false -> it
                true -> null
            }
        }
    }

    fun notifyVideoPlayerNavigationCompleted() {
        openVideoPlayerUrl.value = null
    }
}