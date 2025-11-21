package com.example.lawclientauth

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawclientauth.databinding.ActivitySearchBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 * Global Search (frontend only)
 * - Searches mock lists of lawyers, cases, documents, appointments
 * - Shows suggestions and recent searches (stored in SharedPreferences)
 * - Filter chips to view specific types
 * - Load more button simulates pagination
 *
 * Uses uploaded file path as mock document url: /mnt/data/le-z.zip
 */
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var adapter: SearchAdapter

    // mock master lists
    private val lawyers = mutableListOf<LawyerModel>()
    private val cases = mutableListOf<CaseModel>()
    private val documents = mutableListOf<DocumentModel>()
    private val appointments = mutableListOf<AppointmentModel>()

    // search results + pagination simulation
    private val results = mutableListOf<SearchResultModel>()
    private var page = 0
    private val pageSize = 6

    private val recentKey = "RECENT_SEARCHES"

    // file path provided by developer / uploaded asset
    private val uploadedFilePath = "/mnt/data/le-z.zip"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("LEGAL_EASE_PREFS", MODE_PRIVATE)

        seedMockData()
        setupUI()
        setupRecycler()
        loadRecentSuggestions()
    }

    private fun seedMockData() {
        // Lawyers
        lawyers.clear()
        lawyers.add(LawyerModel("Adv. A Sharma", "Criminal Law", 4.8, 120))
        lawyers.add(LawyerModel("Adv. S Kapoor", "Property Law", 4.6, 86))
        lawyers.add(LawyerModel("Adv. J Rao", "Family Law", 4.9, 210))
        lawyers.add(LawyerModel("Adv. N Gupta", "Corporate Law", 4.5, 50))

        // Cases
        cases.clear()
        cases.add(CaseModel("Property Dispute", "CASE10234", "Open", "Adv. A Sharma", "2025-01-12"))
        cases.add(CaseModel("Criminal Appeal", "CASE10235", "Hearing Soon", "Adv. S Kapoor", "2025-02-10"))
        cases.add(CaseModel("Family Settlement", "CASE10236", "Documents Pending", "Adv. J Rao", "2025-02-05"))
        cases.add(CaseModel("Contract Breach", "CASE10237", "Closed", "Adv. N Gupta", "2024-12-22"))

        // Documents (use uploaded path)
        documents.clear()
        documents.add(DocumentModel("Agreement_Copy.pdf", uploadedFilePath, "CASE10234"))
        documents.add(DocumentModel("Police_Report.pdf", uploadedFilePath, "CASE10235"))
        documents.add(DocumentModel("Court_Notice.pdf", uploadedFilePath, "CASE10236"))
        documents.add(DocumentModel("Contract_Draft.pdf", uploadedFilePath, "CASE10237"))

        // Appointments
        appointments.clear()
        appointments.add(AppointmentModel("Rahul Verma", "Property Dispute Discussion", "12 Feb 2025", "04:30 PM", "Video Call", "Upcoming"))
        appointments.add(AppointmentModel("Aditi Sharma", "Contract Review", "10 Feb 2025", "11:00 AM", "In-Person", "Completed"))
        appointments.add(AppointmentModel("Nikhil Kumar", "Family Court Case", "14 Feb 2025", "03:15 PM", "Upcoming"))
    }

    private fun setupUI() {
        // filter dropdown (All / Lawyers / Cases / Documents / Appointments)
        val filters = listOf("All", "Lawyers", "Cases", "Documents", "Appointments")
        val filterAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filters)
        binding.dropdownFilter.setAdapter(filterAdapter)
        binding.dropdownFilter.setText("All", false)

        binding.dropdownFilter.setOnItemClickListener { _, _, _, _ ->
            doSearch(binding.etSearch.text.toString().trim(), resetPage = true)
        }

        // suggestions dropdown while typing (simple suggestions from combined names)
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val q = p0?.toString()?.trim() ?: ""
                if (q.isEmpty()) {
                    binding.layoutSuggestions.visibility = View.VISIBLE
                    loadRecentSuggestions()
                } else {
                    binding.layoutSuggestions.visibility = View.GONE
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        binding.btnSearch.setOnClickListener {
            doSearch(binding.etSearch.text.toString().trim(), resetPage = true)
        }

        binding.btnClear.setOnClickListener {
            binding.etSearch.text?.clear()
            results.clear()
            adapter.updateList(results)
        }

        binding.btnLoadMore.setOnClickListener {
            page += 1
            doSearch(binding.etSearch.text.toString().trim(), resetPage = false)
        }
    }

    private fun setupRecycler() {
        adapter = SearchAdapter(results,
            onOpenLawyer = { lawyer ->
                // navigate to lawyer profile (we only have LawyerDashboard / ClientProfile; open profile as client profile mock)
                startActivity(Intent(this, ClientProfileActivity::class.java))
            },
            onOpenCase = { caseModel ->
                val intent = Intent(this, CaseDetailsActivity::class.java)
                intent.putExtra("caseId", caseModel.caseId)
                startActivity(intent)
            },
            onOpenDocument = { doc ->
                try {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.setDataAndType(Uri.parse(doc.url), "*/*")
                    startActivity(i)
                } catch (e: Exception) {
                    Toast.makeText(this, "Cannot open document (mock path).", Toast.LENGTH_SHORT).show()
                }
            },
            onOpenAppointment = { appt ->
                // open appointments screen
                startActivity(Intent(this, LawyerAppointmentsActivity::class.java))
            }
        )
        binding.recyclerResults.layoutManager = LinearLayoutManager(this)
        binding.recyclerResults.adapter = adapter
    }

    private fun doSearch(query: String, resetPage: Boolean) {
        if (resetPage) {
            page = 0
            results.clear()
        }

        // get selected filter
        val filter = binding.dropdownFilter.text.toString()

        // if query empty, show recent searches
        if (query.isEmpty()) {
            loadRecentSuggestions()
            return
        }

        // add to recent searches (store top 5)
        saveRecentSearch(query)

        // simulate search across mocks
        val matched = ArrayList<SearchResultModel>()

        if (filter == "All" || filter == "Lawyers") {
            for (l in lawyers) {
                if (l.name.contains(query, ignoreCase = true) || l.specialization.contains(query, ignoreCase = true)) {
                    matched.add(SearchResultModel.lawyer(l))
                }
            }
        }

        if (filter == "All" || filter == "Cases") {
            for (c in cases) {
                if (c.title.contains(query, ignoreCase = true) || c.caseId.contains(query, ignoreCase = true) ) {
                    matched.add(SearchResultModel.caseResult(c))
                }
            }
        }

        if (filter == "All" || filter == "Documents") {
            for (d in documents) {
                if (d.name.contains(query, ignoreCase = true) || d.caseId.contains(query, ignoreCase = true)) {
                    matched.add(SearchResultModel.document(d))
                }
            }
        }

        if (filter == "All" || filter == "Appointments") {
            for (a in appointments) {
                if (a.clientName.contains(query, ignoreCase = true) || a.purpose.contains(query, ignoreCase = true)) {
                    matched.add(SearchResultModel.appointment(a))
                }
            }
        }

        // pagination simulation: show pageSize items per page
        val from = page * pageSize
        val to = kotlin.math.min(from + pageSize, matched.size)
        if (from >= matched.size) {
            // no more items
            Toast.makeText(this, "No more results", Toast.LENGTH_SHORT).show()
            return
        }
        val slice = matched.subList(from, to)
        results.addAll(slice)
        adapter.updateList(results)

        // show/hide load more
        binding.btnLoadMore.visibility = if (to < matched.size) View.VISIBLE else View.GONE
    }

    private fun saveRecentSearch(query: String) {
        val set = prefs.getStringSet(recentKey, LinkedHashSet()) ?: LinkedHashSet()
        // keep insertion order, avoid duplicates
        val newList = LinkedHashSet<String>()
        newList.add(query)
        for (s in set) newList.add(s)
        // keep top 5
        val final = newList.take(5).toSet()
        prefs.edit().putStringSet(recentKey, final).apply()
        loadRecentSuggestions()
    }

    private fun loadRecentSuggestions() {
        val set = prefs.getStringSet(recentKey, LinkedHashSet()) ?: LinkedHashSet()
        val arr = set.toTypedArray()
        binding.listRecent.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)
        binding.layoutSuggestions.visibility = if (arr.isEmpty()) View.GONE else View.VISIBLE

        binding.listRecent.setOnItemClickListener { _, _, pos, _ ->
            val sel = arr[pos]
            binding.etSearch.setText(sel)
            binding.layoutSuggestions.visibility = View.GONE
            doSearch(sel, resetPage = true)
        }
    }
}
