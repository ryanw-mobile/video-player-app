package uk.ryanwong.dazn.codechallenge.base

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import uk.ryanwong.dazn.codechallenge.R

/**
 * Base Fragment to observe on the common LiveData objects.
 * Mainly to control alert dialogs, toasts and snackbar if required
 */
abstract class BaseFragment : Fragment() {
    /**
     * Every fragment has to have an instance of a view model that extends from the BaseViewModel
     */
    abstract val viewModel: BaseViewModel

    override fun onStart() {
        super.onStart()

        viewModel.showErrorMessage.observe(viewLifecycleOwner, { errorMessage ->
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
    }
}