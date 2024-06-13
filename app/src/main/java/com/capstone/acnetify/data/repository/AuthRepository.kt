package com.capstone.acnetify.data.repository

import android.content.SharedPreferences
import com.capstone.acnetify.data.model.UserModel
import com.capstone.acnetify.data.remote.ApiService
import com.capstone.acnetify.data.remote.request.SignInRequest
import com.capstone.acnetify.data.remote.request.SignUpRequest
import com.capstone.acnetify.utils.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Repository class responsible for handling authentication-related operations. This class serves as
 * an abstraction layer between the data sources (API service, SharedPreferences) and the ViewModel.
 *
 * @property apiService Instance of ApiService used to make network requests.
 * @property sharedPreferences Instance of SharedPreferences used to store user authentication data locally.
 */
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) {

    /**
     * Registers a new user with the provided username and password.
     *
     * @param username The username chosen by the user.
     * @param password The password chosen by the user.
     * @return A [Result] indicating the outcome of the sign-up attempt.
     */
    suspend fun signUp(username: String, password: String): Result<UserModel?> {
        val request = SignUpRequest(username, password)
        return try {
            val response = apiService.signUp(request)
            if (response.data != null) {
                Result.Success(response.data)
            } else {
                Result.Error("Unknown error occurred")
            }
        } catch (e: IOException) {
            Result.Error("Couldn't reach server, check your internet connection.")
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.Error("Username or password must be at least 8 characters long and at most 64 characters long")
                409 -> Result.Error("Username already exists")
                else -> Result.Error("Oops, something went wrong!")
            }
        }
    }

    /**
     * Authenticates an existing user with the provided username and password.
     *
     * @param username The username of the user trying to sign in.
     * @param password The password associated with the user's account.
     * @return A [Result] indicating the outcome of the sign-in attempt.
     */
    suspend fun signIn(username: String, password: String): Result<UserModel?> {
        val request = SignInRequest(username, password)
        return try {
            val response = apiService.signIn(request)
            if (response.data != null) {
                // Save authentication data in SharedPreferences
                response.data.apply {
                    saveAuthData(
                        id ?: "",
                        username,
                        token ?: "",
                    )
                }
                Result.Success(response.data)
            } else {
                Result.Error("Unknown error occurred")
            }
        } catch (e: IOException) {
            Result.Error("Couldn't reach server, check your internet connection.")
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.Error("Username or password must be at least 8 characters long and at most 64 characters long")
                401 -> Result.Error("Username or password is incorrect")
                else -> Result.Error("Oops, something went wrong!")
            }
        }
    }

    /**
     * Saves the authentication data (user ID, username, and token) in SharedPreferences.
     *
     * @param userId The ID of the authenticated user.
     * @param username The username of the authenticated user.
     * @param authToken The authentication token of the authenticated user.
     */
    private fun saveAuthData(userId: String, username: String, authToken: String) {
        sharedPreferences.edit().apply {
            putString("user_id", userId)
            putString("username", username)
            putString("auth_token", authToken)
            apply()
        }
    }

    /**
     * Clears the authentication data from SharedPreferences, effectively logging the user out.
     */
    fun clearAuthData() {
        sharedPreferences.edit().clear().apply()
    }

    /**
     * Retrieves the currently logged-in user's data from SharedPreferences.
     *
     * @return A [UserModel] object representing the logged-in user, or null if no user is logged in.
     */
    fun getLoggedInUser(): UserModel? {
        val userId = sharedPreferences.getString("user_id", null)
        val username = sharedPreferences.getString("username", null)
        val authToken = sharedPreferences.getString("auth_token", null)

        return if (userId != null && username != null && authToken != null) {
            UserModel(userId, username, authToken)
        } else {
            null
        }
    }
}