package com.capstone.acnetify.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.acnetify.data.model.ReviewsModel
import com.capstone.acnetify.data.remote.ApiService
import com.capstone.acnetify.utils.MockDataGenerator.generateMockReviews

/**
 * Paging source responsible for loading pages of reviews by acne type data from the API.
 *
 * This class implements the PagingSource interface to load paginated data from the API. It fetches
 * reviews from the API in response to load requests, handling pagination and error cases.
 *
 * @property apiService The ApiService instance used for making network requests.
 */
class ReviewsByAcneTypePagingSource(
    private val apiService: ApiService,
    private val acneType: String,
) : PagingSource<Int, ReviewsModel>() {

    /**
     * Returns the refresh key for the current PagingState.
     *
     * This function calculates the refresh key based on the current anchor position in the PagingState.
     * The refresh key is used to determine the position of the anchor item when refreshing the data set,
     * ensuring a smooth user experience when new data is loaded.
     *
     * @param state The current PagingState, containing information about the loaded data and the anchor position.
     * @return The refresh key, indicating the position of the anchor item.
     */
    override fun getRefreshKey(state: PagingState<Int, ReviewsModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // Find the closest page to anchorPosition.
            val anchorPage = state.closestPageToPosition(anchorPosition)

            // Calculate the refresh key based on the previous or next page key of the anchor page
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    /**
     * Loads a page of review data from the API.
     *
     * This method makes a network request to fetch reviews for the specified page and constructs a
     * LoadResult object containing the loaded data, previous and next page keys, or an error if the
     * request fails.
     *
     * @param params The parameters specifying the load request, including the key for the page to load
     *               and the requested load size.
     * @return A LoadResult object containing the loaded data or an error if the request fails.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewsModel> {
        return try {
            // Determine the page number to load, defaulting to the initial page index if not specified
            val page = params.key ?: INITIAL_PAGE_INDEX
            val limit = params.loadSize
            val offset = (page - 1) * limit

            // Use mock data generator function instead of API call
            val reviews = generateMockReviews(limit)

            // Make a network request to fetch reviews from the API for the specified page
            //val response = apiService.getReviewsByAcneType(acneType, limit, offset)

            // Extract the list of reviews from the API response
            //val reviews = response.data

            // Construct a LoadResult with the loaded data, previous and next page keys
            LoadResult.Page(
                data = reviews,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (reviews.isEmpty()) null else page + 1
            )

        } catch (exception: Exception) {
            // If an error occurs during loading, return an error result
            Log.d(TAG, "Failed to load reviews")
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private val TAG = ReviewsByAcneTypePagingSource::class.java.simpleName

        /** The initial page index for pagination. */
        private const val INITIAL_PAGE_INDEX = 1
    }
}