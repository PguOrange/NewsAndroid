package com.example.newsandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsandroid.R
import com.example.newsandroid.domain.NewsProperty

class NewsAdapter(val items : List<NewsProperty>, private val onClickListener: OnClickListener) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = items[position]
        holder.itemView.setOnClickListener{
            onClickListener.onClick(items)
        }
        holder.bind(items)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
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
                return ViewHolder(
                    view
                )
            }
        }
    }

    class OnClickListener(val clickListener: (newsProperty:NewsProperty) -> Unit) {
        fun onClick(newsProperty:NewsProperty) = clickListener(newsProperty)
    }
}