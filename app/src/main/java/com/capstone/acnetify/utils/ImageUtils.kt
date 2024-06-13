package com.capstone.acnetify.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.capstone.acnetify.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageUtils {

    // Constants
    private const val MAXIMAL_SIZE = 1000000 //1 MB
    private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

    /**
     * Creates a temporary image file in the external cache directory of the application.
     *
     * This function generates a temporary file with a unique name based on the current timestamp.
     * The file is created in the external cache directory, which is a special directory where
     * applications can place cache files they don't need to be visible to the user or other apps.
     * Files stored in this directory may be removed by the system to free up storage space.
     *
     * @param context The context used to access the external cache directory. This is typically the
     * application or activity context.
     *
     * @return A [File] object representing the temporary image file. The file is created in the
     * external cache directory with a unique name ending in ".jpg".
     */
    fun createCustomTempFile(context: Context): File {
        // Get the directory for storing temporary files in the external cache directory
        val filesDir = context.externalCacheDir

        // Create a temporary image file with a timestamp as the filename and .jpg extension
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

    /**
     * Creates a URI for a new image file, handling different storage mechanisms based on the Android version.
     *
     * This function generates a URI for storing a new image file. For devices running Android Q (API level 29) and above,
     * it uses the MediaStore API to store the image in the public gallery, ensuring better integration with the
     * system's media storage. For devices running on Android Pie (API level 28) and below, it falls back to creating
     * a file in the app-specific directory and generating a content URI using the FileProvider API.
     *
     * Example Usage:
     * ```
     * val imageUri = ImageUtils.getImageUri(context)
     * ```
     *
     * @param context The context used to access the file system and the content resolver. This is
     * typically the application or activity context.
     *
     * @return A [Uri] object representing the location where the new image file will be stored.
     * This URI can be used to write image data to the specified location.
     */
    fun getImageUri(context: Context): Uri {
        var uri: Uri? = null

        // For devices running on Android Q and above, save the image in the public gallery
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Create content values for the image file
            val contentValues = ContentValues().apply {
                // Set the display name of the image file using a timestamp
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
                // Set the MIME type of the image file to JPEG
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                // Specify the relative path where the image file will be saved
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
            }

            // Insert the image file into the MediaStore using content resolver
            uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            // Example URI format: content://media/external/images/media/1000000062
            // Example file path: storage/emulated/0/Pictures/MyCamera/20230825_155303.jpg
        }

        // If the URI is null (for pre-Q devices), fallback to getImageUriForPreQ function
        return uri ?: getImageUriForPreQ(context)
    }

    /**
     * Creates a URI for a new image file in the app-specific directory for devices running Android
     * Pie (API level 28) and below.
     *
     * This function handles the creation of a content URI for an image file that will be stored in
     * the app-specific directory (specifically in the "MyCamera" folder within the Pictures directory).
     * It uses the FileProvider API to generate a content URI for the created image file, ensuring that
     * the file is accessible to other apps in a secure manner.
     *
     * Example Usage:
     * ```
     * val imageUri = ImageUtils.getImageUriForPreQ(context)
     * ```
     *
     * @param context The context used to access the file system and the FileProvider. This is
     * typically the application or activity context.
     *
     * @return A [Uri] object representing the location where the new image file will be stored.
     * This URI can be used to write image data to the specified location.
     */
    private fun getImageUriForPreQ(context: Context): Uri {
        // Get the directory for saving images in the external files directory
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Create an image file with a timestamp as the filename
        val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")

        // Create parent directories if they don't exist
        if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()

        // Generate a content URI for the image file using FileProvider
        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            imageFile
        )

        // Example URI format: content://com.capstone.acnetify.fileprovider/my_images/MyCamera/20230825_133659.jpg
    }
}
