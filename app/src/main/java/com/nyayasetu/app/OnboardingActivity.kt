package com.nyayasetu.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.nyayasetu.app.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOnboardingItems()
        setupSkipButton()
    }

    private fun setupOnboardingItems() {
        val onboardingItems = listOf(
            OnboardingItem(
                "Welcome to LegalConnect",
                "Connect with qualified lawyers and\nmanage your legal cases with ease and\nconfidence.",
                "legal consultation",
                "Next"
            ),
            OnboardingItem(
                "Find Expert Lawyers",
                "Browse through our network of verified\nlegal professionals specialized in your\ncase type.",
                "professional lawyers",
                "Next"
            ),
            OnboardingItem(
                "Manage Your Cases",
                "Track case progress, upload documents,\nand stay updated on all legal\nproceedings.",
                "legal documents",
                "Next"
            ),
            OnboardingItem(
                "24/7 Support",
                "Get instant answers from our knowledge\nbase or chat with legal experts anytime.",
                "customer support",
                "Get Started"
            )
        )

        adapter = OnboardingAdapter(onboardingItems, this::onButtonClick)
        viewPager = binding.viewPagerOnboarding
        viewPager.adapter = adapter

        // Add page change callback to update button text
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateSkipButtonVisibility(position)
            }
        })
    }

    private fun setupSkipButton() {
        binding.btnSkip.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun onButtonClick(position: Int) {
        if (position < 3) {
            viewPager.currentItem = position + 1
        } else {
            navigateToMainActivity()
        }
    }

    private fun updateSkipButtonVisibility(position: Int) {
        binding.btnSkip.visibility = if (position == 3) android.view.View.GONE else android.view.View.VISIBLE
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}