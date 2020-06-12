package com.example.newsandroid.ui.topheadlines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsandroid.R
import com.example.newsandroid.domain.NewsProperty

class TopHeadlinesAdapter : RecyclerView.Adapter<TopHeadlinesAdapter.ViewHolder>() {

    var data =  listOf<NewsProperty>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.news_title)
        val author: TextView = itemView.findViewById(R.id.news_author)
        val description: TextView = itemView.findViewById(R.id.news_description)

        fun bind(
            item: NewsProperty
        ) {
            title.text = item.title
            author.text = item.author
            description.text = item.description
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_news, parent, false)
                return ViewHolder(view)
            }
        }
    }
}