package uk.ryanwong.dazn.codechallenge.ui.schedule

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
import uk.ryanwong.dazn.codechallenge.databinding.FragmentScheduleBinding
import uk.ryanwong.dazn.codechallenge.util.setupRefreshLayout

class ScheduleFragment : Fragment() {

    private val scheduleViewModel by viewModels<ScheduleViewModel> {
        ScheduleViewModelFactory(
            (requireContext().applicationContext as DaznApp).apiRepository
        )
    }
    private lateinit var binding: FragmentScheduleBinding
    private val scheduleAdapter = ScheduleAdapter().apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // This eliminates flickering
        setHasStableIds(true)

        // This overrides the adapter's intention to scroll back to the top
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                scheduleViewModel.listState?.let {
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
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.refreshlayout.setOnRefreshListener { scheduleViewModel.refreshSchedule() }
        binding.recyclerview.adapter = scheduleAdapter
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

        scheduleViewModel.showLoading.observe(viewLifecycleOwner, { isRefreshing ->
            binding.refreshlayout.isRefreshing = isRefreshing
        })

        scheduleViewModel.showNoData.observe(viewLifecycleOwner, { isShowNoData ->
            binding.textviewNodata.visibility = when (isShowNoData) {
                true -> View.VISIBLE
                else -> View.GONE
            }
        })

        scheduleViewModel.showErrorMessage.observe(viewLifecycleOwner, { errorMessage ->
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

        scheduleViewModel.scheduleList.observe(viewLifecycleOwner, {
            scheduleViewModel.saveListState(binding.recyclerview.layoutManager?.onSaveInstanceState())
            scheduleAdapter.submitList(it)
        })
    }
}