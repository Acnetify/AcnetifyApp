package com.capstone.acnetify.data.remote.request

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the request body for creating a review.
 *
 * @property acneType The type of acne being reviewed.
 * @property body The body content of the review.
 */
data class CreateReviewRequest(

    @field:SerializedName("acne_type")
    val acneType: String,
    
    @field:SerializedName("body")
    val body: String,
)