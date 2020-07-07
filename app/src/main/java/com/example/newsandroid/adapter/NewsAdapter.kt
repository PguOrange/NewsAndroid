package com.example.newsandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.newsandroid.R
import com.example.newsandroid.domain.NewsProperty
import com.example.newsandroid.enums.NewsListContainer
import com.example.newsandroid.ui.everything.EverythingFragmentDirections
import com.example.newsandroid.ui.topheadlines.TopHeadlinesFragmentDirections
import kotlinx.android.synthetic.main.list_item_news.view.*

class NewsAdapter(private val items : List<NewsProperty>, private val newsListContainer : NewsListContainer) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    private val myNewsListContainer = newsListContainer

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].let { news ->
            with(holder) {
                itemView.tag = news
                bind(news, createOnClickListener(news, position))
            }
        }

    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(
            item: NewsProperty,
            listener : View.OnClickListener

        ) {
            itemView.news_title.text = item.title
            itemView.news_author.text = item.author
            itemView.news_description.text = item.description
            ViewCompat.setTransitionName(itemView.news_title, "title_${position}")
            ViewCompat.setTransitionName(itemView.news_author, "author_${position}")
            ViewCompat.setTransitionName(itemView.news_description, "description_${position}")
            itemView.setOnClickListener(listener)
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

    //class OnClickListener(val clickListener: (newsProperty:NewsProperty) -> Unit) {
    //    fun onClick(newsProperty:NewsProperty){
    //        clickListener(newsProperty)
    //    }
    //}

    private fun createOnClickListener(newsProperty: NewsProperty, position: Int): View.OnClickListener {
        return View.OnClickListener {
            val newsListContainer = if (myNewsListContainer==NewsListContainer.TOPHEADLINES)
                TopHeadlinesFragmentDirections.actionTopHeadlinesFragmentToDetailNewsFragment(newsProperty, position)
            else
                EverythingFragmentDirections.actionEverythingFragmentToDetailNewsFragment(newsProperty, position)
            val extras = FragmentNavigatorExtras(
                it.news_title to "title_$position",
                it.news_author to "author_${position}",
                it.news_description to "description_${position}"
            )

            it.findNavController().navigate(newsListContainer, extras)
        }
    }

}