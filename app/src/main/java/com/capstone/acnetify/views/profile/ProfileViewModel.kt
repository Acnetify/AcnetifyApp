package com.capstone.acnetify.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.acnetify.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    /**
     * Clears authentication data and logs the user out.
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.clearAuthData()
        }
    }

    /**
     * Retrieves the currently logged-in user (if any).
     *
     * You'll likely want to expose this as a LiveData or StateFlow in a real application
     * to observe changes in the login status from your ProfileFragment.
     */
    fun getLoggedInUser() = authRepository.getLoggedInUser()
}