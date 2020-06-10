package com.example.newsandroid.ui.topheadlines

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsandroid.util.NewsItemViewHolder
import com.example.newsandroid.R
import com.example.newsandroid.network.NewsProperty

class TopHeadlinesAdapter : RecyclerView.Adapter<NewsItemViewHolder>() {

    var data =  listOf<NewsProperty>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.news_item_view, parent, false) as TextView
        return NewsItemViewHolder(view)
    }
}