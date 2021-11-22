package uk.ryanwong.dazn.codechallenge.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.ryanwong.dazn.codechallenge.base.BaseRepository
import uk.ryanwong.dazn.codechallenge.base.BaseViewModel
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val baseRepository: BaseRepository) :
    BaseViewModel() {

    // directly expose the list contents from the repository
    val listContents = baseRepository.observeEvents()

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

    override fun refreshList() {
        _showLoading.value = true
        viewModelScope.launch {
            try {
                // Expected exceptions
                baseRepository.refreshEvents()
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