package com.example.lawclientauth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawclientauth.databinding.FaqItemBinding

class FaqAdapter(private val list: List<FaqModel>) :
    RecyclerView.Adapter<FaqAdapter.FaqVH>() {

    inner class FaqVH(val b: FaqItemBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqVH {
        return FaqVH(
            FaqItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FaqVH, position: Int) {
        val f = list[position]

        holder.b.tvQuestion.text = f.question
        holder.b.tvAnswer.text = f.answer
        holder.b.tvAnswer.visibility = if (f.isExpanded) View.VISIBLE else View.GONE

        holder.b.root.setOnClickListener {
            f.isExpanded = !f.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = list.size
}
