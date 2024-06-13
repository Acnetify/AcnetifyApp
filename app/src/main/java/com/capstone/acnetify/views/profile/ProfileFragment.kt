package com.capstone.acnetify.views.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.capstone.acnetify.R
import com.capstone.acnetify.databinding.FragmentProfileBinding
import com.capstone.acnetify.views.auth.welcome.WelcomeActivity
import com.capstone.acnetify.views.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    // View binding instance to access UI elements
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // ViewModel for this fragment, scoped to this fragment's lifecycle
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the logged-in user
        lifecycleScope.launch {
            val loggedInUser = viewModel.getLoggedInUser()
            updateViewState(loggedInUser != null)
        }

        // Set click listener for the button
        binding.btnLogOut.setOnClickListener {
            lifecycleScope.launch {
                val isLoggedIn = viewModel.getLoggedInUser() != null
                if (isLoggedIn) {
                    viewModel.logout()
                    navigateToHomeFragment()
                } else {
                    navigateToWelcomeActivity()
                }
            }
        }
    }

    private fun updateViewState(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            val username = viewModel.getLoggedInUser()?.username ?: ""
            binding.textViewUsername.text = username
            binding.btnLogOut.text = getString(R.string.logout)
        } else {
            binding.textViewUsername.text = "Guest User"
            binding.btnLogOut.text = getString(R.string.sign_in)
        }
    }

    private fun navigateToWelcomeActivity() {
        val intent = Intent(requireContext(), WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHomeFragment() {
        val mainActivity = activity as? MainActivity
        mainActivity?.binding?.bottomNavigationView?.selectedItemId = R.id.home
    }
}