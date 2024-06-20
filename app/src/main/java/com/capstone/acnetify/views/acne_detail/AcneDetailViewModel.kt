package com.capstone.acnetify.views.acne_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.acnetify.data.model.CreateReviewModel
import com.capstone.acnetify.data.model.ReviewsModel
import com.capstone.acnetify.data.remote.response.UpvoteReviewResponse
import com.capstone.acnetify.data.repository.ReviewsRepository
import com.capstone.acnetify.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for handling the logic of the AcneDetail view.
 *
 * @property reviewsRepository The repository for accessing review data.
 */
@HiltViewModel
class AcneDetailViewModel @Inject constructor(
    private val reviewsRepository: ReviewsRepository,
) : ViewModel() {

    private val _createReviewResult: MutableLiveData<Result<CreateReviewModel>> = MutableLiveData()
    val createReviewResult: LiveData<Result<CreateReviewModel>> = _createReviewResult

    private val _upvoteReviewResult: MutableLiveData<Result<UpvoteReviewResponse>> = MutableLiveData()
    val upvoteReviewResult: LiveData<Result<UpvoteReviewResponse>> = _upvoteReviewResult

    private val _cancelUpvoteReviewResult: MutableLiveData<Result<UpvoteReviewResponse>> = MutableLiveData()
    val cancelUpvoteReviewResult: LiveData<Result<UpvoteReviewResponse>> = _cancelUpvoteReviewResult

    /**
     * Retrieves reviews for a specific acne type.
     *
     * @param acneType The type of acne to filter reviews by.
     * @return A LiveData object containing a PagingData of ReviewsModel.
     */
    fun getReviewsByAcneType(acneType: String): LiveData<PagingData<ReviewsModel>> {
        return reviewsRepository.getReviewsByAcneType(acneType)
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    /**
     * Creates a new review for a specific acne type.
     *
     * @param acneType The type of acne the review is for.
     * @param body The content of the review.
     */
    fun createReview(acneType: String, body: String) {
        viewModelScope.launch {
            _createReviewResult.value = Result.Loading
            val result = withContext(Dispatchers.IO) {
                reviewsRepository.createReview(acneType, body)
            }
            _createReviewResult.value = result
        }
    }

    /**
     * Upvotes a review.
     *
     * @param reviewId The ID of the review to upvote.
     */
    fun upvoteReview(reviewId: String) {
        viewModelScope.launch {
            _upvoteReviewResult.value = Result.Loading
            val result = withContext(Dispatchers.IO) {
                reviewsRepository.upvoteReview(reviewId)
            }
            _upvoteReviewResult.value = result
        }
    }

    /**
     * Cancels an upvote for a review.
     *
     * @param reviewId The ID of the review to cancel the upvote for.
     */
    fun cancelUpvoteReview(reviewId: String) {
        viewModelScope.launch {
            _cancelUpvoteReviewResult.value = Result.Loading
            val result = withContext(Dispatchers.IO) {
                reviewsRepository.cancelUpvoteReview(reviewId)
            }
            _cancelUpvoteReviewResult.value = result
        }
    }
}