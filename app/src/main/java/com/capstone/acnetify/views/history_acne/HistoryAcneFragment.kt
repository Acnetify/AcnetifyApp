package com.capstone.acnetify.views.history_acne

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.capstone.acnetify.databinding.FragmentHistoryAcneBinding
import com.capstone.acnetify.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * HistoryAcneFragment is responsible for displaying the user's acne history.
 *
 * This Fragment shows a grid of acne images using a RecyclerView. It uses the Paging library for efficient
 * data loading and handles different loading states to provide a smooth user experience.
 * The ViewModel associated with this Fragment handles data fetching and business logic.
 */
@AndroidEntryPoint
class HistoryAcneFragment : Fragment() {

    // View binding instance to access UI elements
    private var _binding: FragmentHistoryAcneBinding? = null
    private val binding get() = _binding!!

    // Adapter for the RecyclerView to display acne history images
    private val historyAcneAdapter = HistoryAcneAdapter()

    // ViewModel for this fragment, scoped to this fragment's lifecycle
    private val viewModel: HistoryAcneViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryAcneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components and observe data
        setupRecyclerView()
        observeViewModel()
        setupSwipeToRefresh()
    }

    /**
     * Called when the view is destroyed to clean up the view binding and avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Configures the RecyclerView with a GridLayoutManager, fixed size optimization, and the HistoryAcneAdapter.
     */
    private fun setupRecyclerView() {
        // Set up RecyclerView with GridLayoutManager and HistoryAcneAdapter
        binding.rvHistory.apply {
            // Set to grid layout with 2 columns
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = historyAcneAdapter
        }
    }

    /**
     * Observes the ViewModel for changes in the history data and submits new data to the adapter.
     * Also listens for loading state changes to handle UI updates based on data load status.
     */
    private fun observeViewModel() {
        // Observe paginated history data from ViewModel
        viewModel.histories.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
                    binding.swipeRefreshHistory.isRefreshing = true
                    binding.errorImageView.visibility = View.GONE
                    binding.errorTextView.visibility = View.GONE
                    binding.emptyHistoryImage.visibility = View.GONE
                    binding.emptyHistoryText.visibility = View.GONE
                }
                is Result.Success -> {
                    historyAcneAdapter.submitList(result.data)
                    binding.rvHistory.scrollToPosition(0)
                    binding.swipeRefreshHistory.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.errorImageView.visibility = View.GONE
                    binding.errorTextView.visibility = View.GONE

                    if (result.data.isEmpty()) {
                        binding.emptyHistoryImage.visibility = View.VISIBLE
                        binding.emptyHistoryText.visibility = View.VISIBLE
                    } else {
                        binding.emptyHistoryImage.visibility = View.GONE
                        binding.emptyHistoryText.visibility = View.GONE
                    }
                }
                is Result.Error -> {
                    binding.swipeRefreshHistory.isRefreshing = false
                    binding.rvHistory.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.errorImageView.visibility = View.GONE
                    binding.errorTextView.visibility = View.GONE

                    binding.errorTextView.text = result.error
                    binding.errorImageView.visibility = View.VISIBLE
                    binding.errorTextView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshHistory.setOnRefreshListener {
            viewModel.refreshHistory()
        }
    }
}