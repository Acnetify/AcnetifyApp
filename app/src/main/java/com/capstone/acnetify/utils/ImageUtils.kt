package com.capstone.acnetify.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.capstone.acnetify.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
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

    /**
     * Converts a given [imageUri] to a File object.
     *
     * @param imageUri The URI of the image to convert.
     * @param context The context used to access content resolver and file operations.
     * @return A File object representing the image corresponding to the provided URI.
     */
    fun uriToFile(imageUri: Uri, context: Context): File {
        // Create a temporary file to store the converted image.
        val myFile = createCustomTempFile(context)

        // Open an input stream to read data from the image URI.
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream

        // Open an output stream to write data to the temporary file.
        val outputStream = FileOutputStream(myFile)

        // Create a buffer to read data from the input stream.
        val buffer = ByteArray(1024)
        var length: Int

        // Read data from the input stream and write it to the output stream until the end of the stream is reached.
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)

        // Close both input and output streams.
        outputStream.close()
        inputStream.close()

        // Return the created File object.
        return myFile
    }

    /**
     * Reduces the size of the image file to meet a certain maximum size threshold.
     *
     * This function compresses the image file until its size is within the specified maximal size threshold.
     * It iteratively reduces the compression quality until the image size is below the threshold.
     * The compressed image is then written back to the original file.
     *
     * @return A File object representing the reduced image file.
     */
    fun File.reduceFileImage(): File {
        // Initialize variables
        val file = this
        val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
        var compressQuality = 100
        var streamLength: Int

        // Compress the image until its size is within the threshold
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5 // Reduce compression quality
        } while (streamLength > MAXIMAL_SIZE)

        // Write the compressed image back to the file
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }

    /**
     * Retrieves a rotated bitmap based on the orientation information extracted from the given file's Exif data.
     *
     * This function is particularly useful for handling images that may have been captured in different
     * orientations.
     *
     * @param file The File object representing the image file from which the orientation information will be extracted.
     * @return A rotated Bitmap object, adjusted according to the orientation information.
     */
    private fun Bitmap.getRotatedBitmap(file: File): Bitmap {
        // Retrieve the orientation information from the Exif data of the image file
        val orientation = ExifInterface(file).getAttributeInt(
            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
        )

        // Rotate the bitmap based on the orientation information
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
            ExifInterface.ORIENTATION_NORMAL -> this
            else -> this // Return the bitmap as is if no rotation is needed
        }
    }

    /**
     * Rotates the given Bitmap image by the specified angle.
     *
     * @param source The source Bitmap object to be rotated.
     * @param angle The angle of rotation in degrees.
     * @return A new rotated Bitmap object.
     */
    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        // Create a new matrix for rotation
        val matrix = Matrix()
        matrix.postRotate(angle)

        // Apply rotation to the source Bitmap and create a new rotated Bitmap
        return Bitmap.createBitmap(
            source, // Source Bitmap object
            0,      // X coordinate of the top-left corner of the source rectangle
            0,      // Y coordinate of the top-left corner of the source rectangle
            source.width,  // Width of the source rectangle
            source.height, // Height of the source rectangle
            matrix, // Rotation matrix
            true    // Filter to be used when the source pixels are transformed
        )
    }
}
