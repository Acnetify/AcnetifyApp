package com.capstone.acnetify.views.auth.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.acnetify.databinding.ActivityWelcomeBinding
import com.capstone.acnetify.views.auth.sign_in.SignInActivity
import com.capstone.acnetify.views.auth.sign_up.SignUpActivity

/**
 * Activity displayed to the user upon launching the application. Provides options for logging in
 * or signing up.
 */
class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    /**
     * Sets up action listeners for UI elements.
     *
     * In this case, sets up click listeners for the login and signup buttons.
     */
    private fun setupAction() {
        binding.btnSignIn.setOnClickListener {
            // Start the SignInActivity when the login button is clicked
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            // Start the SignUpActivity when the signup button is clicked
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}