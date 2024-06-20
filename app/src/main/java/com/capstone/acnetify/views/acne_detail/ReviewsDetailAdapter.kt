package com.capstone.acnetify.views.acne_detail

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.acnetify.data.model.ReviewsModel
import com.capstone.acnetify.databinding.ItemFeedDetailBinding

class ReviewsDetailAdapter(
    private val onUpvoteClick: (String) -> Unit,
    private val onCancelUpvoteClick: (String) -> Unit
) : PagingDataAdapter<ReviewsModel, ReviewsDetailAdapter.MyViewHolder>(DIFF_CALLBACK) {
    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFeedDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * This method should update the contents of the ViewHolder to reflect the item at the given
     * position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at
     *               the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reviewsModel = getItem(position)
        if (reviewsModel != null) {
            Log.d("ReviewsAdapter", "Binding item at position $position")
            holder.bind(reviewsModel)
        }
    }

    /**
     * ViewHolder class for the RecyclerView.
     *
     * It holds a reference to the [ItemFeedDetailBinding] to bind data to the views.
     */
    inner class MyViewHolder(private val binding: ItemFeedDetailBinding): RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the data from a [ReviewsModel] object to the views.
         *
         * @param reviewsModel The [ReviewsModel] object containing data to be displayed.
         */
        fun bind(reviewsModel: ReviewsModel) {
            // Set the user username, acne type, and description text
            binding.textViewUsername.text = reviewsModel.userUsername
            binding.textViewDescription.text = reviewsModel.body

            // Handle upvote button click
            binding.upvoteButton.setOnClickListener {
                onUpvoteClick(reviewsModel.id!!)
            }

            // Handle cancel upvote button click
            binding.downvoteButton.setOnClickListener {
                onCancelUpvoteClick(reviewsModel.id!!)
            }
        }
    }

    companion object {

        /**
         * DiffUtil.ItemCallback implementation for calculating the difference between two lists of
         * [ReviewsModel] objects. This helps the [PagingDataAdapter] determine the minimum number
         * of changes between an old list and a new list.
         */
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ReviewsModel> =
            object : DiffUtil.ItemCallback<ReviewsModel>() {

                /**
                 * Called to check whether two objects represent the same item.
                 *
                 * @param oldItem The old item.
                 * @param storyItem The new item.
                 * @return True if the two items represent the same object or false if they are different.
                 */
                override fun areItemsTheSame(oldItem: ReviewsModel, storyItem: ReviewsModel): Boolean {
                    return oldItem.acneType == storyItem.acneType
                }

                /**
                 * Called to check whether two items have the same data. This information is used to
                 * detect if the contents of an item have changed.
                 *
                 * @param oldItem The old item.
                 * @param storyItem The new item.
                 * @return True if the contents of the items are the same, false otherwise.
                 */
                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: ReviewsModel, storyItem: ReviewsModel): Boolean {
                    return oldItem == storyItem
                }
            }
    }
}