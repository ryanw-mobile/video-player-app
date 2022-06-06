package uk.ryanwong.dazn.codechallenge.ui.events

import android.os.Parcelable
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.ryanwong.dazn.codechallenge.data.repository.Repository
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val showErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()

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

    val showNoData = Transformations.map(listContents) { list ->
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
        viewModelScope.launch {
            try {
                // Expected exceptions
                repository.refreshEvents()
            } catch (ex: Exception) {
                ex.printStackTrace()
                showErrorMessage.postValue(ex.message)
            }
            _showLoading.postValue(false)
        }
    }

    fun setEventClicked(event: Event) {
        event.videoUrl.let {
            _openVideoPlayerUrl.value = when (it.isEmpty()) {
                false -> it
                true -> null
            }
        }
    }

    fun notifyVideoPlayerNavigationCompleted() {
        _openVideoPlayerUrl.value = null
    }
}