package com.capstone.acnetify.data.remote.request

/**
 * Data class representing the request body for creating a review.
 *
 * @property acneType The type of acne being reviewed.
 * @property body The body content of the review.
 */
data class CreateReviewRequest(
    val acneType: String,
    val body: String,
)