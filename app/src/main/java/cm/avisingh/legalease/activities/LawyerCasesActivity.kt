package cm.avisingh.legalease.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.R
import cm.avisingh.legalease.databinding.ActivityLawyerCasesBinding
import cm.avisingh.legalease.models.LawyerCase
import cm.avisingh.legalease.adapters.LawyerCasesAdapter

class LawyerCasesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLawyerCasesBinding
    private lateinit var casesAdapter: LawyerCasesAdapter
    private val casesList = mutableListOf<LawyerCase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerCasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadDummyCases()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "My Cases"
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        casesAdapter = LawyerCasesAdapter(casesList) { case ->
            // Handle case click
        }
        binding.recyclerViewCases.apply {
            layoutManager = LinearLayoutManager(this@LawyerCasesActivity)
            adapter = casesAdapter
        }
    }

    private fun loadDummyCases() {
        casesList.clear()
        
        val currentTime = System.currentTimeMillis()
        val oneDay = 24 * 60 * 60 * 1000L

        casesList.addAll(listOf(
            LawyerCase(
                id = "case1",
                caseNumber = "CRL/2024/001234",
                title = "State vs. Rajesh Kumar",
                clientName = "Rajesh Kumar",
                caseType = "Criminal",
                court = "District Court, Delhi",
                nextHearing = currentTime + (5 * oneDay),
                status = "Active",
                priority = "High"
            ),
            LawyerCase(
                id = "case2",
                caseNumber = "CIV/2024/005678",
                title = "Property Dispute - Sharma vs. Verma",
                clientName = "Priya Sharma",
                caseType = "Civil",
                court = "High Court, Mumbai",
                nextHearing = currentTime + (10 * oneDay),
                status = "Active",
                priority = "Medium"
            ),
            LawyerCase(
                id = "case3",
                caseNumber = "FAM/2024/002345",
                title = "Divorce Petition - Gupta vs. Gupta",
                clientName = "Amit Gupta",
                caseType = "Family",
                court = "Family Court, Bangalore",
                nextHearing = currentTime + (3 * oneDay),
                status = "Active",
                priority = "High"
            ),
            LawyerCase(
                id = "case4",
                caseNumber = "LAB/2024/003456",
                title = "Employment Dispute - Worker Rights",
                clientName = "Suresh Reddy",
                caseType = "Labour",
                court = "Labour Court, Hyderabad",
                nextHearing = currentTime + (15 * oneDay),
                status = "Active",
                priority = "Low"
            ),
            LawyerCase(
                id = "case5",
                caseNumber = "CRL/2024/004567",
                title = "Bail Application - Theft Case",
                clientName = "Vijay Singh",
                caseType = "Criminal",
                court = "Sessions Court, Pune",
                nextHearing = currentTime + (2 * oneDay),
                status = "Urgent",
                priority = "High"
            ),
            LawyerCase(
                id = "case6",
                caseNumber = "CIV/2024/006789",
                title = "Contract Breach - Business Dispute",
                clientName = "Anil Mehta",
                caseType = "Civil",
                court = "District Court, Ahmedabad",
                nextHearing = currentTime + (20 * oneDay),
                status = "Active",
                priority = "Medium"
            ),
            LawyerCase(
                id = "case7",
                caseNumber = "TAX/2024/001111",
                title = "Tax Evasion Appeal",
                clientName = "Global Tech Pvt Ltd",
                caseType = "Tax",
                court = "Income Tax Tribunal, Delhi",
                nextHearing = currentTime + (30 * oneDay),
                status = "Pending",
                priority = "Medium"
            ),
            LawyerCase(
                id = "case8",
                caseNumber = "CRL/2024/007890",
                title = "Domestic Violence Case",
                clientName = "Meera Devi",
                caseType = "Criminal",
                court = "Magistrate Court, Jaipur",
                nextHearing = currentTime + (7 * oneDay),
                status = "Active",
                priority = "High"
            )
        ))

        casesAdapter.notifyDataSetChanged()
    }
}
