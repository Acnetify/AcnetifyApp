package com.capstone.acnetify.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.capstone.acnetify.data.model.ReviewsModel
import com.capstone.acnetify.data.repository.ReviewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * ViewModel class for the Home screen, responsible for managing UI-related data in a lifecycle-conscious way.
 *
 * This ViewModel interacts with the [ReviewsRepository] to fetch and cache paginated reviews data,
 * exposing it to the UI as [LiveData] streams. It uses the [viewModelScope] to ensure that coroutines
 * are automatically canceled when the ViewModel is cleared.
 *
 * @property reviewsRepository The repository used to fetch reviews data from the API.
 * @constructor Injects the [ReviewsRepository] into the ViewModel using Hilt.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val reviewsRepository: ReviewsRepository,
) : ViewModel() {

    private val currentQuery = MutableStateFlow<String?>(null)

    val allReviews: LiveData<PagingData<ReviewsModel>> = currentQuery.flatMapLatest { query ->
        reviewsRepository.getAllReviews().map { pagingData ->
            if (query.isNullOrEmpty()) {
                pagingData
            } else {
                pagingData.filter {
                    it.acneType?.contains(query, ignoreCase = true) ?: false
                }
            }
        }.cachedIn(viewModelScope)
    }.asLiveData()

    fun searchReviews(query: String?) {
        currentQuery.value = query
    }
}