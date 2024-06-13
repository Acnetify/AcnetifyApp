package com.capstone.acnetify.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.acnetify.data.model.ImageSubmissionsModel
import com.capstone.acnetify.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException

/**
 * Paging source responsible for loading pages of review data history acne type from the API.
 *
 * This class implements the PagingSource interface to load paginated data from the API. It fetches
 * reviews from the API in response to load requests, handling pagination and error cases.
 *
 * @property apiService The ApiService instance used for making network requests.
 */
class HistoryAcneTypePagingSource(private val apiService: ApiService) : PagingSource<Int, ImageSubmissionsModel>() {

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
    override fun getRefreshKey(state: PagingState<Int, ImageSubmissionsModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // Find the closest page to anchorPosition.
            val anchorPage = state.closestPageToPosition(anchorPosition)

            // Calculate the refresh key based on the previous or next page key of the anchor page
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    /**
     * Loads a page of history acne type data from the API.
     *
     * This method makes a network request to fetch reviews for the specified page and constructs a
     * LoadResult object containing the loaded data, previous and next page keys, or an error if the
     * request fails.
     *
     * @param params The parameters specifying the load request, including the key for the page to load
     *               and the requested load size.
     * @return A LoadResult object containing the loaded data or an error if the request fails.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageSubmissionsModel> {
        return try {
            // Determine the page number to load, defaulting to the initial page index if not specified
            val page = params.key ?: INITIAL_PAGE_INDEX
            val limit = params.loadSize

            // Make a network request to fetch history from the API for the specified page
            val response = apiService.getImagesSubmissions()

            // Extract the list of history acne type data from the response
            val data = response.data

            // Calculate nextKey based on whether there's more data
            val nextKey = if (data.size < limit) null else page + 1

            // Construct a LoadResult object with the loaded data, previous and next page keys
            LoadResult.Page(
                data = data,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = nextKey,
            )
        } catch (e: IOException) {
            // Handle network errors
            Log.d(TAG, "Failed to load history acne type: IOException - ${e.message}")
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // Handle HTTP errors with specific codes
            val errorCode = e.code()
            Log.d(TAG, "Failed to load history acne type: HttpException - Code $errorCode")

            when (errorCode) {
                401 -> {
                    // Unauthorized - Trigger login flow
                    // ... (Your logic to initiate login)
                    return LoadResult.Error(e)
                }
                // Handle other error codes as needed
                else -> {
                    return LoadResult.Error(e)
                }
            }
        } catch (e: Exception) {
            // Handle other unexpected exceptions
            Log.d(TAG, "Failed to load history acne type: ${e.message}")
            return LoadResult.Error(e)
        }
    }

    companion object {
        private val TAG = HistoryAcneTypePagingSource::class.java.simpleName

        /** The initial page index for pagination. */
        private const val INITIAL_PAGE_INDEX = 1
    }
}