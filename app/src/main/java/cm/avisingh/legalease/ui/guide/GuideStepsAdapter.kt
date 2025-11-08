package cm.avisingh.legalease.ui.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cm.avisingh.legalease.databinding.ItemGuideStepBinding

class GuideStepsAdapter : ListAdapter<GuideStep, GuideStepsAdapter.ViewHolder>(StepDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGuideStepBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemGuideStepBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(step: GuideStep) {
            binding.apply {
                stepNumberText.text = step.number.toString()
                stepTitleText.text = step.title
                stepDescriptionText.text = step.description
            }
        }
    }
}

private class StepDiffCallback : DiffUtil.ItemCallback<GuideStep>() {
    override fun areItemsTheSame(oldItem: GuideStep, newItem: GuideStep): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: GuideStep, newItem: GuideStep): Boolean {
        return oldItem == newItem
    }
}