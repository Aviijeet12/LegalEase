package com.nyayasetu.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyayasetu.app.databinding.FragmentLawyerManagementBinding

class LawyerManagementFragment : Fragment() {

    private lateinit var binding: FragmentLawyerManagementBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLawyerManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLawyersList()
    }

    private fun setupLawyersList() {
        val lawyers = listOf(
            AdminLawyer(
                "Sarah Johnson",
                "sarah.johnson@legal.com",
                "Employment Law, Corporate Law",
                "4.8",
                "247",
                "88%",
                "Verified"
            ),
            AdminLawyer(
                "Michael Chen",
                "michael.chen@legal.com",
                "Contract Law, Real Estate",
                "4.7",
                "189",
                "85%",
                "Verified"
            ),
            AdminLawyer(
                "Robert Wilson",
                "robert.wilson@legal.com",
                "Family Law, Civil Law",
                "4.6",
                "156",
                "82%",
                "Pending Review"
            )
        )

        val adapter = AdminLawyersAdapter(lawyers)
        binding.lawyersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.lawyersRecyclerView.adapter = adapter
    }
}

data class AdminLawyer(
    val name: String,
    val email: String,
    val specialization: String,
    val rating: String,
    val totalCases: String,
    val winRate: String,
    val status: String
)