package cm.avisingh.legalease.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import cm.avisingh.legalease.R
import cm.avisingh.legalease.adapters.OnboardingAdapter
import cm.avisingh.legalease.databinding.ActivityOnboardingBinding
import cm.avisingh.legalease.models.OnboardingItem

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("OnboardingActivity", "onCreate started")
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOnboarding()
        setupClickListeners()
        Log.d("OnboardingActivity", "onCreate completed")
    }

    private fun setupOnboarding() {
        val onboardingItems = listOf(
            OnboardingItem(
                imageRes = R.drawable.ic_onboarding_1,
                title = getString(R.string.onboarding_title_1),
                description = getString(R.string.onboarding_description_1)
            ),
            OnboardingItem(
                imageRes = R.drawable.ic_onboarding_2,
                title = getString(R.string.onboarding_title_2),
                description = getString(R.string.onboarding_description_2)
            ),
            OnboardingItem(
                imageRes = R.drawable.ic_onboarding_3,
                title = getString(R.string.onboarding_title_3),
                description = getString(R.string.onboarding_description_3)
            )
        )

        onboardingAdapter = OnboardingAdapter(onboardingItems)
        viewPager = binding.viewPagerOnboarding
        viewPager.adapter = onboardingAdapter

        // Add page change callback to update buttons
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateButtons(position)
            }
        })
    }

    private fun setupClickListeners() {
        binding.btnSkip.setOnClickListener {
            navigateToLogin()
        }

        binding.btnNext.setOnClickListener {
            if (viewPager.currentItem < onboardingAdapter.itemCount - 1) {
                // FIXED: Replace with operator assignment
                viewPager.currentItem += 1
            } else {
                navigateToLogin()
            }
        }
    }

    private fun updateButtons(position: Int) {
        if (position == onboardingAdapter.itemCount - 1) {
            binding.btnNext.text = getString(R.string.get_started)
        } else {
            binding.btnNext.text = getString(R.string.next)
        }
    }

    private fun navigateToLogin() {
        try {
            Log.d("OnboardingActivity", "Navigating to LoginActivity")
            val intent = Intent(this, cm.avisingh.legalease.ui.auth.LoginActivity::class.java)
            startActivity(intent)
            Log.d("OnboardingActivity", "LoginActivity started")
            finish()
        } catch (e: Exception) {
            Log.e("OnboardingActivity", "Error navigating to Login", e)
            e.printStackTrace()
        }
    }
}