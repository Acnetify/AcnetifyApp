package com.capstone.acnetify.views.acne_detail

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capstone.acnetify.R
import com.capstone.acnetify.data.model.ImageSubmissionsModel
import com.capstone.acnetify.databinding.ActivityAcneDetailBinding
import com.capstone.acnetify.utils.Result
import com.capstone.acnetify.views.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AcneDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAcneDetailBinding
    private val viewModel: AcneDetailViewModel by viewModels()
    private val feedsAdapter = ReviewsDetailAdapter(
        onUpvoteClick = { reviewId -> viewModel.upvoteReview(reviewId) },
        onCancelUpvoteClick = { reviewId -> viewModel.cancelUpvoteReview(reviewId) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcneDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ensure the EditText does not request focus automatically
        binding.edReview.clearFocus()

        // Ensure the soft keyboard is hidden initially
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.edReview.windowToken, 0)

        // Retrive detail object from the intent extras
        val acneDetail = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_ACNE_ITEM, ImageSubmissionsModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_ACNE_ITEM)
        }
        Log.d(LOG, "acneDetail: $acneDetail")

        acneDetail?.let {
            Glide.with(this)
                .load(it.imageUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .into(binding.previewImageView)

            binding.textViewAcneType.text = when (it.acneType) {
                "acne_nodules" -> "Nodules"
                "milia" -> "Milia"
                "blackhead" -> "Blackhead"
                "whitehead" -> "Whitehead"
                "papula_pustula" -> "Papula & Pustula"
                else -> "Unknown Acne Type"
            }
            binding.textViewDescription.text = when (it.acneType) {
                "acne_nodules" -> getString(R.string.acne_nodule_desc)
                "milia" -> getString(R.string.acne_milia_desc)
                "blackhead" -> getString(R.string.acne_blackhead_desc)
                "whitehead" -> getString(R.string.acne_whitehead_desc)
                "papula_pustula" -> getString(R.string.acne_papula_pustula_desc)
                else -> ""
            }
        }

        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = feedsAdapter
        }

        // Fetch and display reviews
        val acneType = acneDetail?.acneType ?: ""
        viewModel.getReviewsByAcneType(acneType).observe(this) {
            feedsAdapter.submitData(lifecycle, it)
            binding.rvReviews.scrollToPosition(0)
        }
        lifecycleScope.launch {
            feedsAdapter.loadStateFlow.collectLatest {loadStates ->
                handleErrorStates(loadStates)
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        // Handle posting a new review
        binding.btnPost.setOnClickListener {
            val reviewText = binding.edReview.text.toString()
            Log.d(LOG, "btnPost: $acneType")
            viewModel.createReview(acneType, reviewText)

            // Optionally clear the input field and hide the keyboard
            binding.edReview.text?.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.edReview.windowToken, 0)
        }

        setupObservers()

        // Optionally request focus on the EditText after a delay
        Handler(Looper.getMainLooper()).postDelayed({
            binding.edReview.requestFocus()
            imm.showSoftInput(binding.edReview, InputMethodManager.SHOW_IMPLICIT)
        }, 500)
    }

    private fun handleErrorStates(loadStates: CombinedLoadStates) {
        Log.d(LOG, "handleErrorStates: $loadStates")
        val isError = loadStates.refresh is LoadState.Error
        val isLoading = loadStates.refresh is LoadState.Loading

        Log.d(LOG, "handleErrorStates: isLoading = $isLoading, isError = $isError")
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvReviews.visibility = if (isError) View.GONE else View.VISIBLE

        if (isError) {
            val errorState = loadStates.refresh as LoadState.Error
            Log.e(LOG, "LoadState.Error: ${errorState.error.message}")
        }
    }

    private fun setupObservers() {
        viewModel.createReviewResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Thanks for your post!")
                        setMessage("You have successfully post review!")
                        setPositiveButton("Continue") { _, _ ->}
                        // Create and show the AlertDialog
                        create()
                        show()
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Cannot post review!")
                        setMessage("Something wrong when posting your review.")
                        setPositiveButton("Continue") { _, _ ->}
                        // Create and show the AlertDialog
                        create()
                        show()
                    }
                }
            }
        }
        viewModel.upvoteReviewResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    AlertDialog.Builder(this).apply {
                        setTitle("Thanks for your vote!")
                        setMessage("You have successfully vote!")
                        setPositiveButton("Continue") { _, _ ->}
                        // Create and show the AlertDialog
                        create()
                        show()
                    }
                }
                is Result.Error -> {
                    AlertDialog.Builder(this).apply {
                        setTitle("Cannot vote!")
                        setMessage("You already upvoted this review!")
                        setPositiveButton("Continue") { _, _ ->}
                        // Create and show the AlertDialog
                        create()
                        show()
                    }
                }
            }
        }

//        viewModel.cancelUpvoteReviewResult.observe(this) { result ->
//            when (result) {
//                is Result.Loading -> {}
//                is Result.Success -> {
//                    AlertDialog.Builder(this).apply {
//                        setTitle("Thanks for your vote!")
//                        setMessage("You have successfully logged in. Can't wait to start exploring?")
//                        setPositiveButton("Continue") { _, _ ->}
//                        // Create and show the AlertDialog
//                        create()
//                        show()
//                    }
//                }
//                is Result.Error -> {
//                    AlertDialog.Builder(this).apply {
//                        setTitle("Cannot vote!")
//                        setMessage("You already upvoted this review!")
//                        setPositiveButton("Continue") { _, _ ->}
//                        // Create and show the AlertDialog
//                        create()
//                        show()
//                    }
//                }
//            }
//        }
    }

    companion object {
        private val LOG = AcneDetailActivity::class.java.simpleName
        const val EXTRA_ACNE_ITEM = "extra_acne_item"
    }
}