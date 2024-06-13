package com.capstone.acnetify.views.auth.sign_in

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
 * ViewModel responsible for handling the sign-in logic and exposing the sign-in result to the UI.
 *
 * @property authRepository The repository responsible for handling authentication operations.
 */
@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {

    // MutableLiveData to hold the result of the sign-in operation
    private val _signInResult: MutableLiveData<Result<UserModel?>> = MutableLiveData()
    // Public LiveData to expose the sign-in result to the UI
    val signInResult: LiveData<Result<UserModel?>> = _signInResult

    /**
     * Initiates the sign-in process by invoking the sign-in method from the AuthRepository.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     */
    fun signIn(username: String, password: String) {
        // Launch a coroutine in the ViewModel's scope
        viewModelScope.launch {
            // Set the loading state before making the network request
            _signInResult.value = Result.Loading

            // Perform the network request in the IO dispatcher to avoid blocking the main thread
            val result = withContext(Dispatchers.IO) {
                authRepository.signIn(username, password)
            }

            // Update the sign-in result LiveData with the result of the network request
            _signInResult.value = result
        }
    }
}