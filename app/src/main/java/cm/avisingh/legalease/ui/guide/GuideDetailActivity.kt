package cm.avisingh.legalease.ui.guide

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ActivityGuideDetailBinding
import cm.avisingh.legalease.ui.consultation.VideoConsultationActivity
import cm.avisingh.legalease.ui.transitions.FadeInTransition
import cm.avisingh.legalease.ui.transitions.ScaleTransition

class GuideDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuideDetailBinding
    private lateinit var stepsAdapter: GuideStepsAdapter
    private lateinit var documentsAdapter: RequiredDocumentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Postpone transitions until content is loaded
        postponeEnterTransition()

        setupTransitions()
        setupToolbar()
        setupRecyclerViews()
        loadGuideData()
        setupActions()

        // Start transitions after content is loaded
        binding.root.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun setupTransitions() {
        window.enterTransition = FadeInTransition()
        window.exitTransition = FadeInTransition()
        window.sharedElementEnterTransition = ScaleTransition()
        window.sharedElementReturnTransition = ScaleTransition()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerViews() {
        // Setup Steps RecyclerView with animations
        binding.stepsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            stepsAdapter = GuideStepsAdapter()
            adapter = stepsAdapter
            itemAnimator?.apply {
                addDuration = 200
                moveDuration = 200
                changeDuration = 200
                removeDuration = 200
            }
        }

        // Setup Documents RecyclerView with animations
        binding.documentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            documentsAdapter = RequiredDocumentsAdapter { document ->
                showDocumentSample(document)
            }
            adapter = documentsAdapter
            itemAnimator?.apply {
                addDuration = 200
                moveDuration = 200
                changeDuration = 200
                removeDuration = 200
            }
        }
    }

    private fun loadGuideData() {
        // Load guide data from intent
        intent.getStringExtra(EXTRA_GUIDE_ID)?.let { guideId ->
            // For now using sample data
            binding.apply {
                guideImage.transitionName = "guide_image_$guideId"
                guideImage.setImageResource(R.drawable.img_property_registration)
                categoryChip.text = "Property Law"
                titleText.text = "How to Register Property"
                descriptionText.text = "A comprehensive guide on property registration process in India..."
                timeText.text = "2-3 weeks"
                costText.text = "₹15,000-25,000"

                // Animate content appearance
                val contentViews = listOf(
                    categoryChip, titleText, descriptionText,
                    stepsContainer, documentsRecyclerView,
                    consultLawyerButton
                )
                contentViews.forEachIndexed { index, view ->
                    view.alpha = 0f
                    view.translationY = 50f
                    view.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(300)
                        .setStartDelay(100L + index * 50L)
                        .start()
                }
            }

            // Load steps
            val steps = listOf(
                GuideStep(
                    1,
                    "Title Verification",
                    "Verify the property title documents to ensure there are no legal disputes..."
                ),
                GuideStep(
                    2,
                    "Property Valuation",
                    "Get the property valued by a government approved valuer..."
                ),
                GuideStep(
                    3,
                    "Pay Stamp Duty",
                    "Calculate and pay the stamp duty based on property value..."
                ),
                // Add more steps
            )
            stepsAdapter.submitList(steps)

            // Load required documents
            val documents = listOf(
                RequiredDocument(
                    "Sale Deed",
                    R.drawable.ic_document,
                    "sample_sale_deed.pdf"
                ),
                RequiredDocument(
                    "Property Title",
                    R.drawable.ic_document,
                    "sample_property_title.pdf"
                ),
                RequiredDocument(
                    "Tax Receipts",
                    R.drawable.ic_receipt,
                    "sample_tax_receipt.pdf"
                ),
                // Add more documents
            )
            documentsAdapter.submitList(documents)
        }
    }

    private fun setupActions() {
        binding.consultLawyerButton.setOnClickListener {
            startActivity(VideoConsultationActivity.createIntent(this))
        }
    }

    private fun showDocumentSample(document: RequiredDocument) {
        // Show document sample in PDF viewer
        // Will implement this later
    }

    companion object {
        private const val EXTRA_GUIDE_ID = "guide_id"

        fun createIntent(
            activity: Activity,
            guideId: String,
            sharedImageView: View
        ): Intent {
            return Intent(activity, GuideDetailActivity::class.java).apply {
                putExtra(EXTRA_GUIDE_ID, guideId)
            }
        }

        fun start(
            activity: Activity,
            guideId: String,
            sharedImageView: View
        ) {
            val intent = createIntent(activity, guideId, sharedImageView)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                Pair(sharedImageView, "guide_image_$guideId")
            )
            activity.startActivity(intent, options.toBundle())
        }
    }
}

data class GuideStep(
    val number: Int,
    val title: String,
    val description: String
)

data class RequiredDocument(
    val name: String,
    val iconResId: Int,
    val sampleFileUrl: String
)