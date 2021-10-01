package uk.ryanwong.dazn.codechallenge.ui.schedule

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import uk.ryanwong.dazn.codechallenge.data.repository.DaznApiRepository
import uk.ryanwong.dazn.codechallenge.util.SingleLiveEvent

class ScheduleViewModel(private val daznApiRepository: DaznApiRepository) : ViewModel() {

    val showLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val showErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()
    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

    private var _scheduleList = MutableLiveData<List<Schedule>>()
    val scheduleList: LiveData<List<Schedule>>
        get() = _scheduleList

    private var _listState: Parcelable? = null
    val listState: Parcelable?
        get() = _listState

    init {
        // Quietly load the cached contents from local DB before doing a refresh
        // Errors and no data handling can be ignored because refreshEvents() will take care of them
        viewModelScope.launch {
            when (val apiResult = daznApiRepository.getSchedule()) {
                is ApiResult.Success<List<Schedule>> -> {
                    _scheduleList.value =
                        apiResult.data!!   // !! is redundant but added to avoid IDE error
                }
            }
        }
        refreshSchedule()
    }

    fun refreshSchedule() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                // Expected exceptions
                daznApiRepository.refreshSchedule()
            } catch (ex: Exception) {
                ex.printStackTrace()
                showErrorMessage.postValue(ex.toString())
            }

            // Even the previous sync might fail, we still try to fetch whatever we have locally
            when (val apiResult = daznApiRepository.getSchedule()) {
                is ApiResult.Success<List<Schedule>> -> {
                    _scheduleList.value =
                        apiResult.data!!   // !! is redundant but added to avoid IDE error
                    Timber.d("refreshSchedule - fetched ${apiResult.data.size} items to live data")
                }
                is ApiResult.Error -> showErrorMessage.postValue(apiResult.exception.toString())
            }

            //check if no data has to be shown
            invalidateShowNoData()
        }
    }

    fun saveListState(listScrollingState: Parcelable?) {
        _listState = listScrollingState
    }

    /**
     * Inform the user that there's not any data if the list is empty
     */
    private fun invalidateShowNoData() {
        showLoading.postValue(false)
        showNoData.value = _scheduleList.value == null || _scheduleList.value!!.isEmpty()
        Timber.d("invalidateShowNoData - no date = {$showNoData.value}")
    }
}