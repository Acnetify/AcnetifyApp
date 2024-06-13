package com.capstone.acnetify.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.capstone.acnetify.data.model.CreateReviewModel
import com.capstone.acnetify.data.model.ReviewsModel
import com.capstone.acnetify.data.paging.AllReviewsPagingSource
import com.capstone.acnetify.data.paging.ReviewsByAcneTypePagingSource
import com.capstone.acnetify.data.remote.ApiService
import com.capstone.acnetify.data.remote.request.CreateReviewRequest
import com.capstone.acnetify.data.remote.response.UpvoteReviewResponse
import com.capstone.acnetify.utils.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Repository class responsible for fetching review data from the API.
 *
 * This repository interacts with the [ApiService] to fetch reviews and provides paginated data
 * using the Paging 3 library. It supports fetching all reviews sorted by new and fetching
 * reviews by acne type sorted by likes.
 *
 * @property apiService The ApiService instance used for making network requests.
 */
class ReviewsRepository @Inject constructor(
    private var apiService: ApiService,
) {

    /**
     * Fetches all reviews sorted by new.
     *
     * This function returns a Flow of PagingData containing reviews. The reviews are fetched from
     * the API using the AllReviewsPagingSource, which handles pagination and loads data page by page.
     *
     * @return A [Flow] of [PagingData] containing the reviews.
     */
    fun getAllReviews(): Flow<PagingData<ReviewsModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false // Disables placeholders for unloaded items
            ),
            pagingSourceFactory = {
                AllReviewsPagingSource(apiService)
            }
        ).flow
    }

    /**
     * Fetches reviews for a specific acne type sorted by likes.
     *
     * This function returns a Flow of PagingData containing reviews filtered by acne type.
     * The reviews are fetched from the API using the ReviewsByAcneTypePagingSource, which
     * handles pagination and loads data page by page.
     *
     * @param acneType The type of acne for which to fetch reviews.
     * @return A [Flow] of [PagingData] containing the reviews.
     */
    fun getReviewsByAcneType(acneType: String): Flow<PagingData<ReviewsModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false // Disables placeholders for unloaded items
            ),
            pagingSourceFactory = {
                ReviewsByAcneTypePagingSource(apiService, acneType)
            }
        ).flow
    }

    /**
     * Creates a new review.
     *
     * This method sends a request to the API to create a new review with the provided acne type
     * and body content. It handles the network response and returns a Result object indicating
     * success or failure.
     *
     * @param acneType The type of acne being reviewed.
     * @param body The body content of the review.
     * @return A [Result] containing the created review model or an error message.
     */
    suspend fun createReview(acneType: String, body: String): Result<CreateReviewModel> {
        val request = CreateReviewRequest(acneType, body)
        return try {
            val response = apiService.createReview(request)
            if (response.data != null) {
               Result.Success(response.data)
            } else {
                Result.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: IOException) {
            Result.Error(e.message ?: "Couldn't reach server, check your internet connection.")
        } catch (e: HttpException) {
            Result.Error(e.message ?: "Oops, something went wrong!")
        }
    }

    /**
     * Upvotes a review.
     *
     * This method sends a request to the API to upvote a review identified by its review ID.
     * It handles the network response and returns a Result object indicating success or failure.
     *
     * @param reviewId The ID of the review to upvote.
     * @return A [Result] containing the upvote response or an error message.
     */
    suspend fun upvoteReview(reviewId: String): Result<UpvoteReviewResponse> {
        return try {
            val response = apiService.upvoteReview(reviewId)
            Result.Success(response)
        } catch (e: IOException) {
            Result.Error(e.message ?: "Couldn't reach server, check your internet connection.")
        } catch (e: HttpException) {
            Result.Error(e.message ?: "Oops, something went wrong!")
        }
    }

    /**
     * Cancels the upvote on a review.
     *
     * This method sends a request to the API to cancel the upvote on a review identified by its review ID.
     * It handles the network response and returns a Result object indicating success or failure.
     *
     * @param reviewId The ID of the review to cancel the upvote on.
     * @return A [Result] containing the cancel upvote response or an error message.
     */
    suspend fun cancelUpvoteReview(reviewId: String): Result<UpvoteReviewResponse> {
        return try {
            val response = apiService.cancelUpvoteReview(reviewId)
            Result.Success(response)
        } catch (e: IOException) {
            Result.Error(e.message ?: "Couldn't reach server, check your internet connection.")
        } catch (e: HttpException) {
            Result.Error(e.message ?: "Oops, something went wrong!")
        }
    }
}