package com.capstone.acnetify.utils

import com.capstone.acnetify.data.model.ReviewsModel

/**
 * Utility class to generate mock data for different models.
 */
object MockDataGenerator {

    /**
     * Generates a list of mock reviews for testing purposes.
     *
     * @param size The number of mock reviews to generate.
     * @return A list of mock [ReviewsModel] objects.
     */
    fun generateMockReviews(size: Int): List<ReviewsModel> {
        val mockReviews = mutableListOf<ReviewsModel>()
        for (i in 1..size) {
            mockReviews.add(
                ReviewsModel(
                    id = i.toString(),
                    userId = "user_$i",
                    userUsername = "User $i",
                    createdAt = 1716911546804,
                    acneType = "Acne Type $i",
                    body = "This is a review body for review $i",
                    upvote = i * 10,
                    isLiked = true,
                )
            )
        }
        return mockReviews
    }
}