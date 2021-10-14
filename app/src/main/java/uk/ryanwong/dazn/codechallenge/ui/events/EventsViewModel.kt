package uk.ryanwong.dazn.codechallenge.ui.events

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.ryanwong.dazn.codechallenge.base.BaseRepository
import uk.ryanwong.dazn.codechallenge.base.BaseViewModel
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val baseRepository: BaseRepository) :
    BaseViewModel() {

    val openVideoPlayerUrl: SingleLiveEvent<String> = SingleLiveEvent()

    init {
        // Quietly load the cached contents from local DB before doing a refresh
        // Errors and no data handling can be ignored because refreshList() will take care of them
        viewModelScope.launch {
            _listContents.value = baseRepository.getEvents()
        }
        refreshList()
    }

    override fun refreshList() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                // Expected exceptions
                baseRepository.refreshEvents()
            } catch (ex: Exception) {
                ex.printStackTrace()
                showErrorMessage.postValue(ex.message)
            }

            // Even the previous sync might fail, we still try to fetch whatever we have locally
            _listContents.value = baseRepository.getEvents()

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