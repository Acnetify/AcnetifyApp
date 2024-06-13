package com.capstone.acnetify.views.acne_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.acnetify.data.model.ReviewsModel
import com.capstone.acnetify.databinding.ActivityAcneDetailBinding
import com.capstone.acnetify.views.home.ReviewsAdapter

class AcneDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAcneDetailBinding
    private val feedsAdapter = ReviewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcneDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = feedsAdapter
        }

        // Provide mock data
        //feedsAdapter.submitList(getMockData())
    }

    private fun getMockData(): List<ReviewsModel> {
        return listOf(
            ReviewsModel("1", "kisahtegar", 31491, "Acne-A", "2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris", 2, true),
            ReviewsModel("1", "kisahtegar", 31491, "Acne-A", "2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris", 2, true),
            ReviewsModel("1", "kisahtegar", 31491, "Acne-A", "2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris", 2, true),
            ReviewsModel("1", "kisahtegar", 31491, "Acne-A", "2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris", 2, true),
            ReviewsModel("1", "kisahtegar", 31491, "Acne-A", "2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris", 2, true),
            ReviewsModel("1", "kisahtegar", 31491, "Acne-A", "2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris", 2, true),
        )
    }
}