package cm.avisingh.legalease.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.BottomSheetFaqDetailBinding
import cm.avisingh.legalease.ui.guide.GuideDetailActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FAQDetailBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetFaqDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var relatedQuestionsAdapter: FAQAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetFaqDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheet()
        setupRecyclerView()
        loadFAQDetail()
        setupActions()
    }

    private fun setupBottomSheet() {
        // Expand the bottom sheet fully when opened
        view?.doOnPreDraw {
            val behavior = BottomSheetBehavior.from(requireView().parent as View)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setupRecyclerView() {
        binding.relatedQuestionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            relatedQuestionsAdapter = FAQAdapter { faq ->
                // Handle FAQ click - TODO: Implement
            }
            adapter = relatedQuestionsAdapter
        }
    }

    private fun loadFAQDetail() {
        arguments?.getString(ARG_FAQ_ID)?.let { faqId ->
            // Load FAQ details (will be replaced with API call)
            binding.apply {
                titleText.text = "What documents are needed for property registration?"
                answerText.text = """
                    The following documents are required for property registration:
                    
                    1. Sale Deed or Title Document
                    • Original sale deed
                    • Previous ownership documents
                    
                    2. Identity Proof
                    • Aadhaar Card
                    • PAN Card
                    • Voter ID
                    
                    3. Property Details
                    • Property tax receipts
                    • Approved building plan
                    • NOC from society
                    
                    4. Financial Documents
                    • Payment receipts
                    • Bank statements
                    • Loan documents (if applicable)
                    
                    Note: Additional documents may be required based on your location and property type.
                """.trimIndent()

                // Load related questions
                val relatedFAQs = listOf(
                    FAQ(
                        "How long does property registration take?",
                        "The typical duration is 7-15 days..."
                    ),
                    FAQ(
                        "What are the registration charges?",
                        "Registration charges include stamp duty..."
                    ),
                    FAQ(
                        "Is property registration mandatory?",
                        "Yes, property registration is legally required..."
                    )
                )
                relatedQuestionsAdapter.submitList(relatedFAQs)
            }
        }
    }

    private fun setupActions() {
        binding.apply {
            viewGuideButton.setOnClickListener {
                // Navigate to related guide
                dismiss()
                activity?.let {
                    GuideDetailActivity.start(it, "property_registration", it.findViewById(R.id.guideImage))
                }
            }

            consultLawyerButton.setOnClickListener {
                // Navigate to consultation booking
                dismiss()
                // Will implement consultation booking later
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_FAQ_ID = "faq_id"

        fun show(fragmentManager: androidx.fragment.app.FragmentManager, faqId: String) {
            FAQDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_FAQ_ID, faqId)
                }
            }.show(fragmentManager, "FAQ_DETAIL")
        }
    }
}