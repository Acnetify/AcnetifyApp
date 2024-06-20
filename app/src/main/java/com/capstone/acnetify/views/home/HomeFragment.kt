package com.capstone.acnetify.views.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.acnetify.R
import com.capstone.acnetify.databinding.FragmentHomeBinding
import com.capstone.acnetify.views.adapter.LoadingStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * HomeFragment is responsible for displaying the home screen of the application.
 *
 * This Fragment displays a list of reviews using a RecyclerView. It leverages the Paging library
 * for efficient data loading and provides filtering functionality through a dropdown menu.
 * The ViewModel associated with this Fragment handles data fetching and business logic.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    // View binding instance to access UI elements
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Adapter for the RecyclerView to display reviews
    private val feedsAdapter = ReviewsAdapter()

    // ViewModel for this fragment, scoped to this fragment's lifecycle
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the logged-in user
        lifecycleScope.launch {
            val loggedInUser = viewModel.getLoggedInUser()
            updateViewState(loggedInUser != null)
        }

        // Initialize UI components and observe data
        setupRecyclerView()
        observeViewModel()
        setupRefreshListener()
        setupDropdownMenu()
    }

    /**
     * Cleans up the view binding when the view is destroyed to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Configures the RecyclerView with layout manager, adapter, and pagination support.
     */
    private fun setupRecyclerView() {
        // Set up RecyclerView
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = feedsAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {feedsAdapter.retry()}
            )
        }
    }

    /**
     * Observes the ViewModel for review data and handles loading states.
     */
    private fun observeViewModel() {
        // Observe paginated review data from ViewModel
        viewModel.allReviews.observe(viewLifecycleOwner) {
            feedsAdapter.submitData(lifecycle, it)
            binding.rvReviews.scrollToPosition(0)
        }

        // Listen to load state changes and handle error states
        lifecycleScope.launch {
            feedsAdapter.loadStateFlow.collectLatest { loadStates ->
                handleErrorStates(loadStates)
            }
        }
    }

    /**
     * Sets up the refresh listener for the SwipeRefreshLayout to refresh review data.
     */
    private fun setupRefreshListener() {
        binding.swipeRefreshFeeds.setOnRefreshListener {
            feedsAdapter.refresh()
        }
    }

    /**
     * Handles different loading states and displays/hides error UI elements accordingly.
     *
     * @param loadStates The current loading states of the PagingDataAdapter.
     */
    private fun handleErrorStates(loadStates: CombinedLoadStates) {
        val isError = loadStates.refresh is LoadState.Error
        binding.swipeRefreshFeeds.isRefreshing = loadStates.refresh is LoadState.Loading

        // Toggle visibility of the RecyclerView and error UI elements based on the error state
        binding.rvReviews.visibility = if (isError) View.GONE else View.VISIBLE
        binding.errorImageView.visibility = if (isError) View.VISIBLE else View.GONE
        binding.errorTextView.visibility = if (isError) View.VISIBLE else View.GONE

        // If there is an error state, determine the type of error and display an appropriate message
        val errorState = loadStates.refresh as? LoadState.Error
        errorState?.let {
            val errorMessage = when (it.error) {
                is HttpException -> {
                    // Handle HTTP exceptions with specific error codes
                    val errorCode = (it.error as HttpException).code()
                    if (errorCode == 401) "Please log in to view Feeds." else "Error loading data. Please try again."
                }

                is IOException -> "No internet connection."
                else -> "An unexpected error occurred."
            }

            // Set the error message to the TextView to inform the user
            binding.errorTextView.text = errorMessage
        }
    }

    /**
     * Sets up the dropdown menu for filtering reviews by acne type.
     */
    private fun setupDropdownMenu() {
        val acneTypes = resources.getStringArray(R.array.acne_types_array)
        val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_acne, acneTypes)
        binding.acneTypeDropdown.setAdapter(adapter)

        // Set listener for dropdown menu item selection
        binding.acneTypeDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedAcneType = acneTypes[position]
            viewModel.searchReviews(selectedAcneType)
        }
    }

    private fun updateViewState(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            val username = viewModel.getLoggedInUser()?.username ?: ""
            binding.welcomeMessage.text = "Hello, $username"
        } else {
            binding.welcomeMessage.text = "Hello, Guest"
        }
    }
}