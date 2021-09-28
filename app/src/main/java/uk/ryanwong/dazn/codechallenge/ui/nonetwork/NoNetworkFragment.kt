package uk.ryanwong.dazn.codechallenge.ui.nonetwork

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import uk.ryanwong.dazn.codechallenge.databinding.FragmentNonetworkBinding

class NoNetworkFragment : Fragment() {

    private lateinit var noNetworkViewModel: NoNetworkViewModel
    private var _binding: FragmentNonetworkBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noNetworkViewModel =
            ViewModelProvider(this).get(NoNetworkViewModel::class.java)

        _binding = FragmentNonetworkBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        noNetworkViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}