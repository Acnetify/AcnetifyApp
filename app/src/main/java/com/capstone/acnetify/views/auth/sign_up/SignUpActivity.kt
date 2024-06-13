package com.capstone.acnetify.views.auth.sign_up

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.acnetify.databinding.ActivitySignUpBinding
import com.capstone.acnetify.utils.Result
import com.capstone.acnetify.views.auth.sign_in.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupObservers()
    }

    /**
     * Sets up action listeners for UI elements.
     *
     * In this case, sets up click listeners for the login and signup buttons.
     */
    private fun setupAction() {
        binding.btnSignIn.setOnClickListener {
            // Navigate to the SignUpActivity and finish the current activity
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding.btnSignUp.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                signUpViewModel.signUp(username, password)
            } else {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            // Finish the current activity and navigate back to the previous activity (WelcomeActivity)
            finish()
        }
    }

    /**
     * Sets up observers for LiveData from the ViewModel.
     */
    private fun setupObservers() {
        signUpViewModel.signUpResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading indicator
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    // Hide loading indicator
                    binding.progressBar.visibility = View.GONE

                    // Display a confirmation dialog for successful signup
                    AlertDialog.Builder(this@SignUpActivity).apply {
                        setTitle("Account Created!")
                        setMessage("Your account has been successfully created.")
                        setPositiveButton("Continue") { _, _ ->
                            // Navigate to the SignInActivity and finish the current activity
                            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                            finish()
                        }

                        // Create and show the AlertDialog
                        create()
                        show()
                    }
                }

                is Result.Error -> {
                    // Hide loading indicator
                    binding.progressBar.visibility = View.GONE
                    // Show error message
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}