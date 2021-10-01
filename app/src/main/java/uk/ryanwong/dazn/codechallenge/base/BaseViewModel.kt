/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.base

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uk.ryanwong.dazn.codechallenge.util.SingleLiveEvent

/*****
 * ViewModel template for recyclerview layouts
 */
abstract class BaseViewModel : ViewModel() {
    val showLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val showErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()
    val showNoData: MutableLiveData<Boolean> = MutableLiveData()
    protected var _listContents = MutableLiveData<List<Any>>()
    val listContents: LiveData<List<Any>>
        get() = _listContents

    private var _listState: Parcelable? = null
    val listState: Parcelable?
        get() = _listState

    abstract fun refreshList()

    fun saveListState(listScrollingState: Parcelable?) {
        _listState = listScrollingState
    }

    /**
     * Inform the user that there's not any data if the list is empty
     */
    fun invalidateShowNoData() {
        showLoading.postValue(false)
        showNoData.postValue(_listContents.value == null || _listContents.value!!.isEmpty())
    }
}