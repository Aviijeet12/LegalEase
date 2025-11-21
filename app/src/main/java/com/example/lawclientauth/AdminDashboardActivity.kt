package com.example.lawclientauth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lawclientauth.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var adapter: AdminOptionAdapter
    private val options = mutableListOf<AdminOptionModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadOptions()
        setupRecycler()
    }

    private fun loadOptions() {
        options.add(AdminOptionModel("Manage Lawyers", R.drawable.ic_profile_placeholder))
        options.add(AdminOptionModel("Manage Clients", R.drawable.ic_case))
        options.add(AdminOptionModel("Approve KYC", R.drawable.ic_document))
        options.add(AdminOptionModel("Handle Disputes", R.drawable.ic_alert))
        options.add(AdminOptionModel("Reports", R.drawable.ic_calendar))
        options.add(AdminOptionModel("Admin Profile", R.drawable.ic_profile_placeholder))
    }

    private fun setupRecycler() {
        adapter = AdminOptionAdapter(options) { option ->
            when (option.title) {
                "Manage Lawyers" ->
                    startActivity(Intent(this, AdminLawyerListActivity::class.java))

                "Manage Clients" ->
                    startActivity(Intent(this, AdminClientListActivity::class.java))

                "Approve KYC" ->
                    startActivity(Intent(this, AdminKycActivity::class.java))

                "Handle Disputes" ->
                    startActivity(Intent(this, AdminDisputeActivity::class.java))

                "Reports" ->
                    startActivity(Intent(this, AdminReportsActivity::class.java))

                "Admin Profile" ->
                    startActivity(Intent(this, AdminProfileActivity::class.java))
            }
        }

        binding.recyclerAdminOptions.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerAdminOptions.adapter = adapter
    }
}
