package com.capstone.acnetify.views.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.capstone.acnetify.R
import com.capstone.acnetify.databinding.ActivityMainBinding
import com.capstone.acnetify.views.acne_types.AcneTypesFragment
import com.capstone.acnetify.views.camera.CameraActivity
import com.capstone.acnetify.views.history_acne.HistoryAcneFragment
import com.capstone.acnetify.views.home.HomeFragment
import com.capstone.acnetify.views.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    /**
     * Permission request launcher for camera permission.
     *
     * This launcher handles the result of the camera permission request. It displays a toast
     * message indicating whether the permission request was granted or denied.
     */
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Display a toast message indicating that the permission request was granted
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show()
            } else {
                // Display a toast message indicating that the permission request was denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
            }
        }

    /**
     * Checks if all required permissions are granted.
     *
     * This function checks whether the camera permission is granted to the application.
     * @return true if the camera permission is granted, false otherwise.
     */
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.background = null

        // Request camera permission if not granted
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        // Set the initial fragment
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        @Suppress("DEPRECATION")
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.acneTypes -> {
                    replaceFragment(AcneTypesFragment())
                    true
                }
                R.id.history -> {
                    replaceFragment(HistoryAcneFragment())
                    true
                }
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        binding.fab.setOnClickListener {
            lifecycleScope.launch {
                val loggedInUser = viewModel.getLoggedInUser()
                navigateToCamera(loggedInUser != null)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    private fun navigateToCamera(loggedInUser: Boolean) {
        if (loggedInUser) {
            startActivity(Intent(this, CameraActivity::class.java))
        } else {
            AlertDialog.Builder(this)
                .setTitle("Failed to open camera")
                .setMessage("You need to login before adding new acne!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    companion object {
        // Required camera permission constant
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}