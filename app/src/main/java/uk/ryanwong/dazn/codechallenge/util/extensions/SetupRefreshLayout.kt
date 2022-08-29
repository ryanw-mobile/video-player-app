/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.util.extensions

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import uk.ryanwong.dazn.codechallenge.R

fun Fragment.setupRefreshLayout(
    refreshLayout: SwipeRefreshLayout,
    refreshListener: SwipeRefreshLayout.OnRefreshListener
) {
    refreshLayout.setColorSchemeColors(
        ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
        ContextCompat.getColor(requireActivity(), R.color.colorAccent),
        ContextCompat.getColor(requireActivity(), R.color.colorPrimaryDark)
    )
    refreshLayout.setOnRefreshListener(refreshListener)
}
