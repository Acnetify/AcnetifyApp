package com.capstone.acnetify.views.main

import androidx.lifecycle.ViewModel
import com.capstone.acnetify.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel(){
    /**
     * Retrieves the currently logged-in user (if any).
     *
     * You'll likely want to expose this as a LiveData or StateFlow in a real application
     * to observe changes in the login status from your ProfileFragment.
     */
    fun getLoggedInUser() = authRepository.getLoggedInUser()
}