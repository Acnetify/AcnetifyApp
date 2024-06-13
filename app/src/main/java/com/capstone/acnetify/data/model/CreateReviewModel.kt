package com.capstone.acnetify.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the model of a created review.
 *
 * @property postUpvote The number of upvotes the review has received.
 * @property userId The ID of the user who created the review.
 * @property userUsername The username of the user who created the review.
 * @property createdAt The timestamp when the review was created.
 * @property acneType The type of acne being reviewed.
 * @property postBody The body content of the review.
 */
data class CreateReviewModel(

	@field:SerializedName("post_upvote")
	val postUpvote: Int? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("user_username")
	val userUsername: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("acne_type")
	val acneType: String? = null,

	@field:SerializedName("post_body")
	val postBody: String? = null
)
