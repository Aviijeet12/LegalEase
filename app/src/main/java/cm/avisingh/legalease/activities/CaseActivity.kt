package cm.avisingh.legalease.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cm.avisingh.legalease.R
import cm.avisingh.legalease.api.ApiClient
import kotlinx.coroutines.launch
import cm.avisingh.legalease.utils.AnalyticsHelper

class CaseActivity : AppCompatActivity() {
    private lateinit var analyticsHelper: AnalyticsHelper
    private lateinit var listViewCases: ListView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case)

        analyticsHelper.trackScreen(this::class.java.simpleName)

        initializeViews()
        loadCases()
    }

    private fun initializeViews() {
        listViewCases = findViewById(R.id.listViewCases)
        progressBar = findViewById(R.id.progressBar)

        listViewCases.setOnItemClickListener { parent, view, position, id ->
            val caseTitle = listViewCases.adapter.getItem(position) as String
            Toast.makeText(this, "Selected: $caseTitle", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCases() {
        lifecycleScope.launch {
            try {
                progressBar.visibility = android.view.View.VISIBLE
                val response = ApiClient.instance.getCases()

                if (response.success && response.data != null) {
                    val caseTitles = response.data.map { it.title }
                    val adapter = ArrayAdapter(
                        this@CaseActivity,
                        android.R.layout.simple_list_item_1,
                        caseTitles
                    )
                    listViewCases.adapter = adapter
                    Toast.makeText(this@CaseActivity, "Loaded ${caseTitles.size} cases", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CaseActivity, "Failed to load cases", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CaseActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = android.view.View.GONE
            }
        }
    }
}