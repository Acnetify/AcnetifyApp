package com.capstone.acnetify.data.remote.response

import com.capstone.acnetify.data.model.CreateReviewModel
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response for creating a review.
 *
 * @property message A message indicating the outcome of the create review attempt.
 * @property data The created review data.
 */
data class CreateReviewResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: CreateReviewModel? = null,
)