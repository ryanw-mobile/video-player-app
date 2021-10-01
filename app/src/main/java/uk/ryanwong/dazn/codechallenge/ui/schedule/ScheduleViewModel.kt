package uk.ryanwong.dazn.codechallenge.ui.schedule

import android.os.CountDownTimer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import uk.ryanwong.dazn.codechallenge.base.BaseViewModel
import uk.ryanwong.dazn.codechallenge.data.ApiResult
import uk.ryanwong.dazn.codechallenge.data.model.Schedule
import uk.ryanwong.dazn.codechallenge.data.repository.DaznApiRepository

class ScheduleViewModel(private val daznApiRepository: DaznApiRepository) : BaseViewModel() {

    private val timer: CountDownTimer = object : CountDownTimer(COUNTDOWN_TIME, COUNTDOWN_TIME) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            Timber.d("Timer triggered")
            refreshList()
            autoRefresh()
        }
    }

    init {
        // Quietly load the cached contents from local DB before doing a refresh
        // Errors and no data handling can be ignored because refreshList() will take care of them
        viewModelScope.launch {
            when (val apiResult = daznApiRepository.getSchedule()) {
                is ApiResult.Success<List<Schedule>> -> {
                    _listContents.value =
                        apiResult.data
                }
            }
        }
        refreshList()
        autoRefresh()
    }

    // Needs to cancel the timer when ViewModel is destroyed to prevent memory leak
    override fun onCleared() {
        timer.cancel()
        Timber.d("Timer cancelled")
        super.onCleared()
    }

    override fun refreshList() {
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
                    _listContents.value =
                        apiResult.data
                    Timber.d("refreshSchedule - fetched ${apiResult.data.size} items to live data")
                }
                is ApiResult.Error -> showErrorMessage.postValue(apiResult.exception.toString())
            }

            //check if no data has to be shown
            invalidateShowNoData()
        }
    }

    // This is to repeat the timer automatically
    // it cannot be done within CountDownTimer's onFinish() as compiler said timer is not initialized
    private fun autoRefresh() {
        timer.start()
    }

    companion object {
        // This is the auto refresh interval
        const val COUNTDOWN_TIME = 30000L
    }
}