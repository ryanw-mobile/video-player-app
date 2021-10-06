package uk.ryanwong.dazn.codechallenge.base

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import uk.ryanwong.dazn.codechallenge.R
import uk.ryanwong.dazn.codechallenge.util.filterErrorMessage

/**
 * Base Fragment to observe on the common LiveData objects.
 * Mainly to control alert dialogs, toasts and snackbar if required
 */
abstract class BaseFragment : Fragment() {
    /**
     * Every fragment has to have an instance of a view model that extends from the BaseViewModel
     */
    abstract val viewModel: BaseViewModel

    private var errorDialog: AlertDialog? = null

    override fun onStart() {
        super.onStart()

        viewModel.showErrorMessage.observe(viewLifecycleOwner, { errorMessage ->
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
                errorDialog!!.show()
            }
        })
    }
}