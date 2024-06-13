package com.capstone.acnetify.data.remote.response

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response for up-voting a review.
 *
 * @property message A message indicating the outcome of the upvote attempt.
 */
data class UpvoteReviewResponse(

    @field:SerializedName("message")
    val message: String? = null
)