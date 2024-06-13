package com.capstone.acnetify.views.auth.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.acnetify.data.model.UserModel
import com.capstone.acnetify.data.repository.AuthRepository
import com.capstone.acnetify.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel magnificently responsible for handling the sign-up process and elegantly exposing the
 * sign-up results to the UI.
 *
 * @property authRepository The delightful repository responsible for handling authentication operations.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {

    // MutableLiveData to hold the astonishing result of the sign-up operation
    private val _signUpResult: MutableLiveData<Result<UserModel?>> = MutableLiveData()
    // Public LiveData to gracefully expose the sign-up result to the UI
    val signUpResult: LiveData<Result<UserModel?>> = _signUpResult

    /**
     * Initiates the enchanting sign-up process by invoking the sign-up method from the AuthRepository.
     *
     * @param username The eagerly chosen username by the user.
     * @param password The securely chosen password by the user.
     */
    fun signUp(username: String, password: String) {
        // Launch a coroutine in the ViewModel's splendid scope
        viewModelScope.launch {
            // Set the exhilarating loading state before making the network request
            _signUpResult.value = Result.Loading

            // Perform the network request in the IO dispatcher to avoid blocking the main thread
            val result = withContext(Dispatchers.IO) {
                authRepository.signUp(username, password)
            }

            // Update the sign-up result LiveData with the mesmerizing result of the network request
            _signUpResult.value = result
        }
    }
}