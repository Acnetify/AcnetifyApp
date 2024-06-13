package com.capstone.acnetify.views.history_acne

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.acnetify.data.model.ImageSubmissionsModel
import com.capstone.acnetify.data.repository.ImageRepository
import com.capstone.acnetify.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryAcneViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
) : ViewModel() {

    private val _histories = MutableLiveData<Result<List<ImageSubmissionsModel>>>()
    val histories: LiveData<Result<List<ImageSubmissionsModel>>> = _histories

    init {
        viewModelScope.launch {
            _histories.value = Result.Loading
            _histories.value = imageRepository.getImagesSubmissions()
        }
    }

    fun refreshHistory() {
        viewModelScope.launch {
            _histories.value = Result.Loading
            _histories.value = imageRepository.getImagesSubmissions()
        }
    }
}