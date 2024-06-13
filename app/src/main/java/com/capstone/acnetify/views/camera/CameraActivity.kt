package com.capstone.acnetify.views.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.capstone.acnetify.databinding.ActivityCameraBinding
import com.capstone.acnetify.utils.ImageUtils
import com.capstone.acnetify.views.acne_upload.AcneUploadActivity

/**
 * A simple activity to capture images using CameraX.
 *
 * This activity allows users to switch between front and back cameras, capture images,
 * and save them as temporary files.
 */
class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var currentImageUri: Uri? = null

    /**
     * Listens for changes in device orientation.
     *
     * This lazy-initialized property creates an OrientationEventListener to detect changes in device
     * orientation. It adjusts the target rotation of the ImageCapture instance accordingly to capture
     * images correctly.
     */
    private val orientationEventListener by lazy {
        // Create an anonymous inner class extending OrientationEventListener
        object : OrientationEventListener(this) {

            /**
             * Invoked when device orientation changes.
             *
             * This method adjusts the target rotation of the ImageCapture instance based on the
             * device orientation.
             *
             * @param orientation: The new orientation angle in degrees.
             */
            override fun onOrientationChanged(orientation: Int) {
                // If the orientation is unknown, do nothing
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                // Determine the rotation based on the orientation angle
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                // Set the target rotation of the ImageCapture instance
                imageCapture?.targetRotation = rotation
            }
        }
    }

    /**
     * Launcher for the gallery activity result.
     *
     * This variable registers an activity result launcher for picking visual media, such as images,
     * from the gallery. When an image is selected from the gallery, the launcher callback assigns
     * the URI of the selected image to the currentImageUri property.
     */
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            // Assigns the URI of the selected image to the currentImageUri property
            currentImageUri = uri

            // Start UploadActivity with the selected image URI
            startUploadActivity(uri)
        } else {
            // Prints a debug log message if no media is selected
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup setOnClickListener
        binding.btnBack.setOnClickListener { finish() }
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.btnSwitchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA

            startCamera()
        }
        binding.btnGallery.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    /**
     * Called when the activity is becoming visible to the user.
     *
     * Registers the orientation event listener to enable orientation changes detection.
     */
    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    /**
     * Called when the activity is no longer visible to the user.
     *
     * Unregisters the orientation event listener to stop detecting orientation changes.
     */
    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    /**
     * Called when the activity is resumed. Used to hide system UI and start the camera.
     */
    override fun onResume() {
        super.onResume()
        startCamera()
    }

    /**
     * Initializes and starts the camera.
     *
     * This function initializes the camera provider, sets up the preview configuration, and binds
     * the camera to the activity's lifecycle. In case of any errors, it displays a toast message
     * with the error and logs the error message.
     */
    private fun startCamera() {
        // Initialize the camera provider
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        // Listen for the camera provider initialization completion
        cameraProviderFuture.addListener({
            // Get the initialized camera provider
            val cameraProvider = cameraProviderFuture.get()

            // Set up the preview configuration
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Set up the image capture configuration
            imageCapture = ImageCapture.Builder().build()

            try {
                // Unbind any previously bound use cases
                cameraProvider.unbindAll()

                // Bind the camera use cases to the activity's lifecycle
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector, // Select the desired camera (front or back)
                    preview,        // Provide the preview configuration
                    imageCapture    // Provide the image capture configuration
                )
            } catch (e: Exception) {
                // Handle exceptions by displaying a toast message and logging the error
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${e.message}")
            }
        },ContextCompat.getMainExecutor(this))
    }

    /**
     * Captures a photo using the camera.
     *
     * This function captures a photo using the ImageCapture instance, saves it to a temporary file,
     * and handles the success or failure scenarios of capturing the image.
     */
    private fun takePhoto() {
        // Ensure that the ImageCapture instance is not null
        val imageCapture = imageCapture ?: return

        // Create a temporary file to save the captured photo
        val photoFile = ImageUtils.createCustomTempFile(application)

        // Configure the output options for saving the captured photo
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Capture the photo with specified output options and handle success or failure scenarios
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // Retrieve the URI of the saved image
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    // Start UploadActivity with the saved image URI
                    startUploadActivity(savedUri)
                }

                override fun onError(e: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${e.message}")
                }
            }
        )
    }

    /**
     * Starts the UploadActivity with the given image URI.
     *
     * This function creates an intent to start the UploadActivity and passes the image URI
     * as an extra to the intent.
     *
     * @param imageUri: The URI of the image to be uploaded.
     */
    private fun startUploadActivity(imageUri: Uri) {
        val intent = Intent(this, AcneUploadActivity::class.java).apply {
            putExtra(EXTRA_CAMERAX_IMAGE, imageUri.toString())
        }
        startActivity(intent)
    }

    /**
     * Companion object holding constants and shared properties for CameraActivity.
     */
    companion object {
        val TAG: String = CameraActivity::class.java.simpleName
        // Extra key for passing captured image URI between activities
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        // Result code for camera operations
        //const val CAMERAX_RESULT = 200
    }
}