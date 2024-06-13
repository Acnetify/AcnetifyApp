package com.capstone.acnetify.views.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.acnetify.data.model.ReviewsModel
import com.capstone.acnetify.databinding.ItemFeedBinding

/**
 * Adapter class for displaying a list of [ReviewsModel] objects in a RecyclerView.
 *
 * This adapter uses [PagingDataAdapter] for efficient updates and a DiffUtil callback to compute
 * the difference between lists.
 */
class ReviewsAdapter: PagingDataAdapter<ReviewsModel, ReviewsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
           holder.bind(reviewsModel)
        }
    }

    /**
     * ViewHolder class for the RecyclerView.
     *
     * It holds a reference to the [ItemFeedBinding] to bind data to the views.
     */
    class MyViewHolder(private val binding: ItemFeedBinding): RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the data from a [ReviewsModel] object to the views.
         *
         * @param reviewsModel The [ReviewsModel] object containing data to be displayed.
         */
        fun bind(reviewsModel: ReviewsModel) {
            // Set the user username, acne type, and description text
            binding.textViewUsername.text = reviewsModel.userUsername
            binding.textViewAcne.text = reviewsModel.acneType
            binding.textViewDescription.text = reviewsModel.body

            // Set click listener for item, potentially for navigation to a detail activity
            binding.root.setOnClickListener {
                // Uncomment the code below and implement the intent to navigate to DetailAcneActivity
                // val intent = Intent(binding.root.context, DetailAcneActivity::class.java).apply {
                //     putExtra(DetailAcneActivity.EXTRA_STORY_ITEM, history)
                // }
                // binding.root.context.startActivity(intent)
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