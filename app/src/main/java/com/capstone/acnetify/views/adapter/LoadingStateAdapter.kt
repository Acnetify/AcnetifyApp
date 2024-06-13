package com.capstone.acnetify.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.acnetify.databinding.ItemLoadingBinding

/**
 * Adapter for displaying loading and error states in a RecyclerView.
 *
 * @param retry Callback function to retry loading when in error state.
 */
class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    /**
     * Creates a new LoadingStateViewHolder instance.
     *
     * @param parent The parent view group.
     * @param loadState The current load state.
     * @return LoadingStateViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }

    /**
     * Binds data to the LoadingStateViewHolder based on the current load state.
     *
     * @param holder The LoadingStateViewHolder to bind data to.
     * @param loadState The current load state.
     */
    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    /**
     * ViewHolder for the LoadingStateAdapter.
     *
     * Represents the loading and error states in the RecyclerView.
     *
     * @param binding View binding for the item layout.
     * @param retry Callback function to retry loading when in error state.
     */
    class LoadingStateViewHolder(
        private val binding: ItemLoadingBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            // Set click listener for retry button
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        /**
         * Binds data to the item view based on the current load state.
         *
         * @param loadState The current load state.
         */
        fun bind(loadState: LoadState) {
            // Show progress bar when loading, retry button and error message when in error state
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }
}