package com.capstone.acnetify.views.acne_types

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capstone.acnetify.R
import com.capstone.acnetify.data.model.AcneTypesModel
import com.capstone.acnetify.databinding.ItemAcneTypeBinding

/**
 * Adapter class for displaying a list of [AcneTypesModel] objects in a RecyclerView.
 *
 * This adapter uses ListAdapter for efficient updates and a DiffUtil callback to compute
 * the difference between lists.
 */
class AcneTypesAdapter: ListAdapter<AcneTypesModel, AcneTypesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAcneTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * This method should update the contents of the ViewHolder to reflect the item at the given
     * position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder class for the RecyclerView.
     *
     * It holds a reference to the ItemAcneTypeBinding to bind data to the views.
     */
    class MyViewHolder(private val binding: ItemAcneTypeBinding): RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the data from an AcneImageModel object to the views.
         *
         * @param acneTypes The AcneImageModel object containing data to be displayed.
         */
        fun bind(acneTypes: AcneTypesModel) {
            // Set the acne type & description text
            binding.textViewAcneType.text = when (acneTypes.acneType) {
                "acne_nodules" -> "Nodules"
                "milia" -> "Milia"
                "blackhead" -> "Blackhead"
                "whitehead" -> "Whitehead"
                "papula_pustula" -> "Papula & Pustula"
                else -> "Unknown Acne Type"
            }
            binding.textViewAcneDescription.text = acneTypes.description

            // Load the acne image using Glide
            Glide.with(binding.imageViewAcne.context)
                .load(acneTypes.imageResId)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .into(binding.imageViewAcne)


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
         * AcneTypeModel objects. This helps the ListAdapter determine the minimum number of changes
         * between an old list and a new list.
         */
        val DIFF_CALLBACK: DiffUtil.ItemCallback<AcneTypesModel> =
            object : DiffUtil.ItemCallback<AcneTypesModel>() {

                /**
                 * Called to check whether two objects represent the same item.
                 *
                 * @param oldItem The old item.
                 * @param storyItem The new item.
                 * @return True if the two items represent the same object or false if they are different.
                 */
                override fun areItemsTheSame(oldItem: AcneTypesModel, storyItem: AcneTypesModel): Boolean {
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
                override fun areContentsTheSame(oldItem: AcneTypesModel, storyItem: AcneTypesModel): Boolean {
                    return oldItem == storyItem
                }
            }
    }
}