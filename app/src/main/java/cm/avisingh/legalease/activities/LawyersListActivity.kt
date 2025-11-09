package cm.avisingh.legalease.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.adapters.LawyersAdapter
import cm.avisingh.legalease.databinding.ActivityLawyersListBinding
import cm.avisingh.legalease.models.Lawyer

class LawyersListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLawyersListBinding
    private lateinit var lawyersAdapter: LawyersAdapter
    private val allLawyers = mutableListOf<Lawyer>()
    private val filteredLawyers = mutableListOf<Lawyer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadDummyLawyers()
        setupRecyclerView()
        setupSearchAndFilters()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Find a Lawyer"
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun loadDummyLawyers() {
        allLawyers.clear()
        allLawyers.addAll(
            listOf(
                Lawyer(
                    id = "1",
                    name = "Adv. Rajesh Kumar",
                    specialization = "Property & Civil Law",
                    experience = "15+ years",
                    rating = 4.8f,
                    totalCases = 250,
                    email = "rajesh.kumar@legalease.com",
                    phone = "+91 98765 43210",
                    location = "Delhi",
                    about = "Specialized in property disputes, civil litigation, and contract law with extensive court experience.",
                    languages = listOf("Hindi", "English", "Punjabi"),
                    availability = "Available"
                ),
                Lawyer(
                    id = "2",
                    name = "Adv. Priya Sharma",
                    specialization = "Family & Divorce Law",
                    experience = "12+ years",
                    rating = 4.7f,
                    totalCases = 180,
                    email = "priya.sharma@legalease.com",
                    phone = "+91 98765 43211",
                    location = "Mumbai",
                    about = "Expert in family law, divorce cases, child custody, and matrimonial disputes.",
                    languages = listOf("Hindi", "English", "Marathi"),
                    availability = "Available"
                ),
                Lawyer(
                    id = "3",
                    name = "Adv. Arjun Patel",
                    specialization = "Criminal Law",
                    experience = "18+ years",
                    rating = 4.9f,
                    totalCases = 320,
                    email = "arjun.patel@legalease.com",
                    phone = "+91 98765 43212",
                    location = "Ahmedabad",
                    about = "Senior criminal lawyer with expertise in bail applications, criminal trials, and appeals.",
                    languages = listOf("Hindi", "English", "Gujarati"),
                    availability = "Busy"
                ),
                Lawyer(
                    id = "4",
                    name = "Adv. Sneha Reddy",
                    specialization = "Corporate & Business Law",
                    experience = "10+ years",
                    rating = 4.6f,
                    totalCases = 150,
                    email = "sneha.reddy@legalease.com",
                    phone = "+91 98765 43213",
                    location = "Hyderabad",
                    about = "Specialized in corporate law, mergers & acquisitions, and business contracts.",
                    languages = listOf("Hindi", "English", "Telugu"),
                    availability = "Available"
                ),
                Lawyer(
                    id = "5",
                    name = "Adv. Vikram Singh",
                    specialization = "Property & Real Estate Law",
                    experience = "14+ years",
                    rating = 4.7f,
                    totalCases = 200,
                    email = "vikram.singh@legalease.com",
                    phone = "+91 98765 43214",
                    location = "Bangalore",
                    about = "Expert in real estate transactions, property disputes, and land acquisition matters.",
                    languages = listOf("Hindi", "English", "Kannada"),
                    availability = "Available"
                ),
                Lawyer(
                    id = "6",
                    name = "Adv. Meera Iyer",
                    specialization = "Employment & Labour Law",
                    experience = "11+ years",
                    rating = 4.5f,
                    totalCases = 160,
                    email = "meera.iyer@legalease.com",
                    phone = "+91 98765 43215",
                    location = "Chennai",
                    about = "Specialized in employment disputes, labour laws, and workplace harassment cases.",
                    languages = listOf("Hindi", "English", "Tamil"),
                    availability = "Available"
                ),
                Lawyer(
                    id = "7",
                    name = "Adv. Rohit Verma",
                    specialization = "Civil & Consumer Law",
                    experience = "13+ years",
                    rating = 4.6f,
                    totalCases = 190,
                    email = "rohit.verma@legalease.com",
                    phone = "+91 98765 43216",
                    location = "Pune",
                    about = "Expert in civil litigation, consumer disputes, and defamation cases.",
                    languages = listOf("Hindi", "English", "Marathi"),
                    availability = "Available"
                ),
                Lawyer(
                    id = "8",
                    name = "Adv. Kavita Nair",
                    specialization = "Immigration & Visa Law",
                    experience = "9+ years",
                    rating = 4.4f,
                    totalCases = 120,
                    email = "kavita.nair@legalease.com",
                    phone = "+91 98765 43217",
                    location = "Kochi",
                    about = "Specialized in immigration law, visa applications, and citizenship matters.",
                    languages = listOf("Hindi", "English", "Malayalam"),
                    availability = "Busy"
                ),
                Lawyer(
                    id = "9",
                    name = "Adv. Amit Desai",
                    specialization = "Tax & GST Law",
                    experience = "16+ years",
                    rating = 4.8f,
                    totalCases = 220,
                    email = "amit.desai@legalease.com",
                    phone = "+91 98765 43218",
                    location = "Mumbai",
                    about = "Expert in tax litigation, GST matters, and financial law.",
                    languages = listOf("Hindi", "English", "Gujarati"),
                    availability = "Available"
                ),
                Lawyer(
                    id = "10",
                    name = "Adv. Anjali Chopra",
                    specialization = "Intellectual Property Law",
                    experience = "8+ years",
                    rating = 4.5f,
                    totalCases = 100,
                    email = "anjali.chopra@legalease.com",
                    phone = "+91 98765 43219",
                    location = "Delhi",
                    about = "Specialized in trademark, patent, and copyright law.",
                    languages = listOf("Hindi", "English"),
                    availability = "Available"
                )
            )
        )
        filteredLawyers.clear()
        filteredLawyers.addAll(allLawyers)
    }

    private fun setupRecyclerView() {
        lawyersAdapter = LawyersAdapter(filteredLawyers) { lawyer ->
            openContactLawyer(lawyer)
        }
        binding.rvLawyers.apply {
            layoutManager = LinearLayoutManager(this@LawyersListActivity)
            adapter = lawyersAdapter
        }
        updateEmptyState()
    }

    private fun setupSearchAndFilters() {
        // Search functionality
        binding.etSearch.addTextChangedListener { text ->
            filterLawyers(text.toString(), getSelectedFilter())
        }

        // Filter chips
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val searchQuery = binding.etSearch.text.toString()
            val filter = when (checkedId) {
                binding.chipCivil.id -> "Civil"
                binding.chipCriminal.id -> "Criminal"
                binding.chipFamily.id -> "Family"
                binding.chipCorporate.id -> "Corporate"
                else -> "All"
            }
            filterLawyers(searchQuery, filter)
        }
    }

    private fun getSelectedFilter(): String {
        return when (binding.chipGroup.checkedChipId) {
            binding.chipCivil.id -> "Civil"
            binding.chipCriminal.id -> "Criminal"
            binding.chipFamily.id -> "Family"
            binding.chipCorporate.id -> "Corporate"
            else -> "All"
        }
    }

    private fun filterLawyers(query: String, category: String) {
        filteredLawyers.clear()

        val searchResults = if (query.isEmpty()) {
            allLawyers
        } else {
            allLawyers.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.specialization.contains(query, ignoreCase = true)
            }
        }

        val categoryResults = if (category == "All") {
            searchResults
        } else {
            searchResults.filter {
                it.specialization.contains(category, ignoreCase = true)
            }
        }

        filteredLawyers.addAll(categoryResults)
        lawyersAdapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (filteredLawyers.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.rvLawyers.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.rvLawyers.visibility = View.VISIBLE
        }
    }

    private fun openContactLawyer(lawyer: Lawyer) {
        val intent = Intent(this, ContactLawyerActivity::class.java).apply {
            putExtra("lawyer_id", lawyer.id)
            putExtra("lawyer_name", lawyer.name)
            putExtra("lawyer_specialization", lawyer.specialization)
            putExtra("lawyer_experience", lawyer.experience)
            putExtra("lawyer_rating", lawyer.rating)
            putExtra("lawyer_email", lawyer.email)
            putExtra("lawyer_phone", lawyer.phone)
        }
        startActivity(intent)
    }
}
