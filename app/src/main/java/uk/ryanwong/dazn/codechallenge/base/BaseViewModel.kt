/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.base

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import uk.ryanwong.dazn.codechallenge.util.SingleLiveEvent

/*****
 * ViewModel template for recyclerview layouts
 */
abstract class BaseViewModel : ViewModel() {
    val showErrorMessage: SingleLiveEvent<String> = SingleLiveEvent()

    // This is to maintain the recyclerview scrolling state during list refresh
    private var _listState: Parcelable? = null
    val listState: Parcelable?
        get() = _listState

    fun saveListState(listScrollingState: Parcelable?) {
        _listState = listScrollingState
    }

    abstract fun refreshList()
}