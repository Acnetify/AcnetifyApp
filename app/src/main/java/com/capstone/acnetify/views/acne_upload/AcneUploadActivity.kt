package com.capstone.acnetify.views.acne_upload

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.acnetify.databinding.ActivityAcneUploadBinding
import com.capstone.acnetify.utils.ImageUtils
import com.capstone.acnetify.utils.ImageUtils.reduceFileImage
import com.capstone.acnetify.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AcneUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAcneUploadBinding
    private val viewModel: AcneUploadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcneUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.uploadAcneImageResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this)
                        .setTitle("Success")
                        .setMessage("Acne successfully predicted is ${result.data.data}")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }
                        .create()
                        .show()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(result.error)
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
            }
        }

        // Retrieve the image URI from the Intent
        val imageUri = intent.getStringExtra(EXTRA_CAMERAX_IMAGE)
        if (imageUri != null) {
            // Load the image using Glide
            Glide.with(this)
                .load(Uri.parse(imageUri))
                .into(binding.previewImageView)

            val imageFile = ImageUtils.uriToFile(Uri.parse(imageUri), this).reduceFileImage()
            Log.d(TAG, "showImage: ${imageFile.path}")
            binding.uploadButton.setOnClickListener {
                viewModel.uploadAcneImage(imageFile)
            }
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    companion object {
        private val TAG = AcneUploadActivity::class.java.simpleName
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
    }
}