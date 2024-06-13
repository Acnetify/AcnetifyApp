package com.capstone.acnetify.data.remote

import com.capstone.acnetify.data.remote.request.CreateReviewRequest
import com.capstone.acnetify.data.remote.request.SignInRequest
import com.capstone.acnetify.data.remote.request.SignUpRequest
import com.capstone.acnetify.data.remote.response.CreateReviewResponse
import com.capstone.acnetify.data.remote.response.ImagesByAcneTypeResponse
import com.capstone.acnetify.data.remote.response.ImagesSubmissionsResponse
import com.capstone.acnetify.data.remote.response.ReviewsResponse
import com.capstone.acnetify.data.remote.response.SignInResponse
import com.capstone.acnetify.data.remote.response.SignUpResponse
import com.capstone.acnetify.data.remote.response.UploadAcneImageResponse
import com.capstone.acnetify.data.remote.response.UpvoteReviewResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface defining endpoints for communicating with the remote API.
 */
interface ApiService {

    /**
     * Makes a request to register a new user.
     *
     * @param request The sign-up request containing user information.
     * @return A response indicating the outcome of the sign-up attempt.
     */
    @POST("/auth/register")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): SignUpResponse

    /**
     * Makes a request to authenticate an existing user.
     *
     * @param request The sign-in request containing user credentials.
     * @return A response indicating the outcome of the sign-in attempt.
     */
    @POST("/auth/login")
    suspend fun signIn(
        @Body request: SignInRequest
    ): SignInResponse

    /**
     * Fetches all reviews sorted by new.
     *
     * @param limit The maximum number of reviews to fetch.
     * @param offset The number of reviews to skip before starting to fetch.
     * @return A list of reviews.
     */
    @GET("/review")
    suspend fun getAllReviews(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
    ): ReviewsResponse

    /**
     * Fetches reviews for a specific acne type sorted by likes.
     *
     * @param acneType The type of acne for which to fetch reviews.
     * @param limit The maximum number of reviews to fetch.
     * @param offset The number of reviews to skip before starting to fetch.
     * @return A list of reviews.
     */
    @GET("/review/{acne_type}")
    suspend fun getReviewsByAcneType(
        @Path("acne_type") acneType: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
    ): ReviewsResponse

    /**
     * Creates a new review.
     *
     * @param request The request body containing the review details.
     * @return A response indicating the outcome of the create review attempt.
     */
    @POST("/review")
    suspend fun createReview(
        @Body request: CreateReviewRequest,
    ): CreateReviewResponse

    /**
     * Upvote a review.
     *
     * @param reviewId The ID of the review to upvote.
     * @return A response indicating the outcome of the upvote attempt.
     */
    @PUT("/review/{reviewId}/upvote")
    suspend fun upvoteReview(
        @Path("reviewId") reviewId: String,
    ): UpvoteReviewResponse

    /**
     * Cancels an upvote on a review.
     *
     * @param reviewId The ID of the review to cancel the upvote on.
     * @return A response indicating the outcome of the cancel upvote attempt.
     */
    @PUT("/review/{reviewId}/cancel-upvote")
    suspend fun cancelUpvoteReview(
        @Path("reviewId") reviewId: String,
    ): UpvoteReviewResponse

    /**
     * Uploads an image related to acne.
     *
     * This method makes a multipart POST request to upload an image file.
     *
     * @param image The image file to be uploaded.
     * @return A response indicating the outcome of the image upload attempt.
     */
    @Multipart
    @POST("/image/upload")
    suspend fun uploadAcneImage(
        @Part image: MultipartBody.Part,
    ): UploadAcneImageResponse

    /**
     * Fetches all image submissions.
     *
     * This method makes a GET request to retrieve all submitted images.
     *
     * @return A response containing the list of all image submissions.
     */
    @GET("/image")
    suspend fun getImagesSubmissions(): ImagesSubmissionsResponse

    /**
     * Fetches images related to a specific acne type.
     *
     * This method makes a GET request to retrieve images associated with a specific type of acne.
     *
     * @param acneType The type of acne for which to fetch images.
     * @return A response containing the list of images for the specified acne type.
     */
    @GET("/image/{acne_type}")
    suspend fun getImagesByAcneType(
        @Path("acne_type") acneType: String,
    ): ImagesByAcneTypeResponse
}