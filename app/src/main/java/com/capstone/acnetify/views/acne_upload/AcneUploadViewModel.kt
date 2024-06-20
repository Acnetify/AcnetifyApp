package com.capstone.acnetify.views.acne_upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.acnetify.data.remote.response.UploadAcneImageResponse
import com.capstone.acnetify.data.repository.ImageRepository
import com.capstone.acnetify.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AcneUploadViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
) : ViewModel(){

    private val _uploadAcneImageResult: MutableLiveData<Result<UploadAcneImageResponse>> = MutableLiveData()
    val uploadAcneImageResult: LiveData<Result<UploadAcneImageResponse>> = _uploadAcneImageResult

    fun uploadAcneImage(imageFile: File) {
        viewModelScope.launch {
            _uploadAcneImageResult.value = Result.Loading
            val result = withContext(Dispatchers.IO) {
                imageRepository.uploadAcneImage(imageFile)
            }
            _uploadAcneImageResult.value = result
        }
    }
}