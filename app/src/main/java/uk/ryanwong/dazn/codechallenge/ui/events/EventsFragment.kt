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
    private val eventsAdapter = EventsAdapter(EventClickListener {
        eventsViewModel.setEventClicked(it)
    }).apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // This eliminates flickering
        setHasStableIds(true)

        // This overrides the adapter's intention to scroll back to the top
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                eventsViewModel.listState?.let {
                    // layoutManager.scrollToPositionWithOffset(position, 0)
                    binding.recyclerview.layoutManager?.onRestoreInstanceState(it)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.refreshlayout.setOnRefreshListener { eventsViewModel.refreshEvents() }
        binding.recyclerview.adapter = eventsAdapter
        binding.recyclerview.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
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
                AlertDialog.Builder(requireContext(), R.style.MyAlertDialogStyle).apply {
                    setTitle(getString(R.string.something_went_wrong))
                    setMessage(errorMessage)
                    setPositiveButton(getString(R.string.ok)) { _, _ ->
                        // do nothing
                    }
                }.show()
            }
        })

        eventsViewModel.eventList.observe(viewLifecycleOwner, {
            eventsViewModel.saveListState(binding.recyclerview.layoutManager?.onSaveInstanceState())
            eventsAdapter.submitList(it)
        })
    }
}