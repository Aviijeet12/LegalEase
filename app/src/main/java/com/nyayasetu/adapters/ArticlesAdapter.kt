package com.nyayasetu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyayasetu.app.R

data class Article(
    val id: String = "",
    val title: String = "",
    val summary: String = "",
    val category: String = "",
    val datePublished: String = ""
)

class ArticlesAdapter(
    private var articles: List<Article>,
    private val onArticleClick: (Article) -> Unit = {}
) : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
        holder.itemView.setOnClickListener { onArticleClick(article) }
    }

    override fun getItemCount() = articles.size

    fun updateData(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.itemTitle)
        private val summaryText: TextView = itemView.findViewById(R.id.itemDescription)

        fun bind(article: Article) {
            titleText.text = article.title
            summaryText.text = article.summary
        }
    }
}