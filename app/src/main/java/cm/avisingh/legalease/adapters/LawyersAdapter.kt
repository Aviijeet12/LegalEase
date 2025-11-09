package cm.avisingh.legalease.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemLawyerBinding
import cm.avisingh.legalease.models.Lawyer

class LawyersAdapter(
    private val lawyers: List<Lawyer>,
    private val onLawyerClick: (Lawyer) -> Unit
) : RecyclerView.Adapter<LawyersAdapter.LawyerViewHolder>() {

    inner class LawyerViewHolder(private val binding: ItemLawyerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lawyer: Lawyer) {
            binding.apply {
                // Set avatar with initials
                val initials = lawyer.name.split(" ")
                    .take(2)
                    .joinToString("") { it.first().toString() }
                    .uppercase()
                tvAvatar.text = initials

                tvLawyerName.text = lawyer.name
                tvSpecialization.text = lawyer.specialization
                ratingBar.rating = lawyer.rating
                tvRating.text = lawyer.rating.toString()
                tvTotalCases.text = "${lawyer.totalCases}+ cases"
                tvExperience.text = lawyer.experience
                tvAvailability.text = lawyer.availability

                btnContactLawyer.setOnClickListener {
                    onLawyerClick(lawyer)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerViewHolder {
        val binding = ItemLawyerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LawyerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LawyerViewHolder, position: Int) {
        holder.bind(lawyers[position])
    }

    override fun getItemCount() = lawyers.size
}
