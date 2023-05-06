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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uk.ryanwong.dazn.codechallenge.R
import uk.ryanwong.dazn.codechallenge.databinding.FragmentEventsBinding
import uk.ryanwong.dazn.codechallenge.domain.models.Event
import uk.ryanwong.dazn.codechallenge.util.extensions.setupRefreshLayout
import uk.ryanwong.dazn.codechallenge.util.filterErrorMessage

@AndroidEntryPoint
class EventsFragment : Fragment() {

    private val viewModel: EventsViewModel by viewModels()
    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            viewModel.listState?.let {
                // layoutManager.scrollToPositionWithOffset(position, 0)
                binding.recyclerview.layoutManager?.onRestoreInstanceState(it)
            }
        }
    }

    private val eventsAdapter = EventsAdapter(
        EventClickListener {
            viewModel.setEventClicked(it)
        }
    ).apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // This eliminates flickering
        setHasStableIds(true)

        // This overrides the adapter's intention to scroll back to the top
        registerAdapterDataObserver(adapterDataObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The purpose of LifecycleObserver is to eliminate writing the boilerplate code
        // to load and cleanup resources in onCreate() and onDestroy()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerview.apply {
            adapter = eventsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        this.setupRefreshLayout(binding.refreshlayout) { viewModel.refreshList() }
    }

    private var errorDialog: AlertDialog? = null
    override fun onStart() {
        super.onStart()

        viewModel.showErrorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotBlank()) {
                // make sure we only show one latest dialog to users for better UX:
                errorDialog?.dismiss()

                // Show an error dialog
                errorDialog =
                    AlertDialog.Builder(requireContext(), R.style.MyAlertDialogStyle).apply {
                        setTitle(getString(R.string.something_went_wrong))
                        setMessage(filterErrorMessage(requireContext(), errorMessage))
                        setPositiveButton(getString(R.string.ok)) { _, _ ->
                            // do nothing
                        }
                    }.create()
                errorDialog?.show()
            }
        }

        viewModel.showLoading.observe(viewLifecycleOwner) { isRefreshing ->
            binding.refreshlayout.isRefreshing = isRefreshing
        }

        viewModel.showNoData.observe(viewLifecycleOwner) { isShowNoData ->
            binding.textviewNodata.visibility = when (isShowNoData) {
                true -> View.VISIBLE
                else -> View.GONE
            }
        }

        viewModel.listContents.observe(viewLifecycleOwner) {
            viewModel.saveListState(binding.recyclerview.layoutManager?.onSaveInstanceState())
            eventsAdapter.submitList(it as List<Event>)
        }

        viewModel.openVideoPlayerUrl.observe(viewLifecycleOwner) { videoUrl ->
            videoUrl?.let {
                findNavController().navigate(
                    EventsFragmentDirections.actionNavigationEventsToExoplayerActivity(
                        videoUrl
                    )
                )
                viewModel.notifyVideoPlayerNavigationCompleted()
            }
        }
    }

    override fun onDestroyView() {
        eventsAdapter.apply {
            try {
                unregisterAdapterDataObserver(adapterDataObserver)
            } catch (ex: IllegalStateException) {
                // If the observer is not registered
                ex.printStackTrace()
            }
        }
        binding.recyclerview.apply {
            adapter = null
        }
        _binding = null
        super.onDestroyView()
    }
}
