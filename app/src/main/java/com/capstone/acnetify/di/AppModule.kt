package com.capstone.acnetify.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.capstone.acnetify.data.remote.ApiService
import com.capstone.acnetify.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * AppModule provides application-level dependencies.
 *
 * It is installed in the SingletonComponent, meaning the provided instances will live as long
 * as the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Creates an instance of HttpLoggingInterceptor for logging HTTP request and response data.
     * The log level is set to BODY, which logs request and response lines and their respective
     * headers and bodies (if present).
     */
    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(sharedPreferences: SharedPreferences): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val token = sharedPreferences.getString("auth_token", null) // Retrieve token

            val requestBuilder = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token") // Add token to header (if available)
                .method(originalRequest.method, originalRequest.body)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor) // Add the auth interceptor
            .build()
    }

    /**
     * Provides an instance of ApiService using Retrofit.
     *
     * The Retrofit instance is configured with a base URL, a Gson converter factory for JSON
     * serialization/deserialization, and an OkHttpClient with logging capabilities.
     *
     * @return An instance of ApiService for making API calls.
     */
    @Provides
    @Singleton
    fun providesApiService(okHttpClient: OkHttpClient) : ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    /**
     * Provides an instance of SharedPreferences.
     *
     * The SharedPreferences instance is used for storing and retrieving user authentication data locally.
     * The preferences are stored in private mode to ensure they are only accessible by this application.
     *
     * @param app The application context used to access the SharedPreferences.
     * @return An instance of SharedPreferences for persistent data storage.
     */
    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences(
            Constants.SHARED_PREF_NAME,
            MODE_PRIVATE
        )
    }
}