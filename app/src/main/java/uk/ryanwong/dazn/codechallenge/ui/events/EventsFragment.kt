/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.events

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import uk.ryanwong.dazn.codechallenge.DaznApp
import uk.ryanwong.dazn.codechallenge.R
import uk.ryanwong.dazn.codechallenge.databinding.FragmentEventsBinding
import uk.ryanwong.dazn.codechallenge.util.setupRefreshLayout

class EventsFragment : Fragment() {

    private val eventsViewModel by viewModels<EventsViewModel> {
        EventsViewModelFactory(
            (requireContext().applicationContext as DaznApp).apiRepository
        )
    }
    private lateinit var binding: FragmentEventsBinding
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.refreshlayout.setOnRefreshListener { eventsViewModel.refreshEvents() }

        eventsAdapter = EventsAdapter(EventClickListener {
            eventsViewModel.setEventClicked(it)
        })
        eventsAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        eventsAdapter.setHasStableIds(true) // This eliminates flickering

        // This overrides the adapter's intention to scroll back to the top
        eventsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                eventsViewModel.listState?.let {
                    // layoutManager.scrollToPositionWithOffset(position, 0)
                    binding.recyclerview.layoutManager?.onRestoreInstanceState(it)
                }
            }
        })

        binding.recyclerview.adapter = eventsAdapter

        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.recyclerview.addItemDecoration(dividerItemDecoration)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The purpose of LifecycleObserver is to eliminate writing the boilerplate code
        // to load and cleanup resources in onCreate() and onDestory()
        binding.lifecycleOwner = viewLifecycleOwner
        this.setupRefreshLayout(binding.refreshlayout)

        eventsViewModel.showLoading.observe(viewLifecycleOwner, { isRefreshing ->
            binding.refreshlayout.isRefreshing = isRefreshing
        })

        eventsViewModel.showNoData.observe(viewLifecycleOwner, { isShowNoData ->
            binding.textviewNodata.visibility = when (isShowNoData) {
                true -> View.VISIBLE
                else -> View.GONE
            }
        })

        eventsViewModel.showErrorMessage.observe(viewLifecycleOwner, { errorMessage ->
            if (errorMessage.isNotBlank()) {
                // Show an error dialog
                AlertDialog.Builder(requireContext(),R.style.MyAlertDialogStyle)
                    .setTitle(getString(R.string.something_went_wrong))
                    .setMessage(errorMessage)
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        // do nothing
                    }
                    .show()
            }
        })

        eventsViewModel.eventList.observe(viewLifecycleOwner, {
            eventsViewModel.saveListState(binding.recyclerview.layoutManager?.onSaveInstanceState())
            eventsAdapter.submitList(it)
        })
    }
}