package com.capstone.acnetify.views.acne_upload

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.acnetify.databinding.ActivityAcneUploadBinding

class AcneUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAcneUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcneUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the image URI from the Intent
        val imageUri = intent.getStringExtra(EXTRA_CAMERAX_IMAGE)
        if (imageUri != null) {
            // Load the image using Glide
            Glide.with(this)
                .load(Uri.parse(imageUri))
                .into(binding.previewImageView)
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
    }
}