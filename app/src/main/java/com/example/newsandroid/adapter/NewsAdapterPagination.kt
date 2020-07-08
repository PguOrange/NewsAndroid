package com.example.newsandroid.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.newsandroid.R
import com.example.newsandroid.domain.NewsProperty
import com.example.newsandroid.enums.NewsListContainer
import com.example.newsandroid.ui.everything.EverythingFragmentDirections
import com.example.newsandroid.ui.topheadlines.TopHeadlinesFragmentDirections
import kotlinx.android.synthetic.main.list_item_news.view.*

class NewsAdapterPagination (private var items : ArrayList<NewsProperty>, newsListContainer : NewsListContainer) : RecyclerView.Adapter<BaseViewHolder>() {


    override fun getItemCount() = items.size

    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    private val myNewsListContainer = newsListContainer
    private var isLoaderVisible = false


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        items[position].let { news ->
            with(holder) {
                itemView.tag = news
                bind(news, createOnClickListener(news, position), position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_NORMAL -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_news, parent, false)
            )
            VIEW_TYPE_LOADING -> ProgressHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_loading, parent, false)
            )
            else -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_news, parent, false)
            )
        }
    }

    fun addNewItems(postItems: List<NewsProperty>) {
        items.clear()
        items.addAll(postItems)
        notifyDataSetChanged()
    }
    fun addItems(postItems: List<NewsProperty>) {
        items.addAll(postItems)
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
        items.add(NewsProperty())
        notifyItemInserted(items.size-1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position: Int = items.size-1
        val item: NewsProperty = getItem(position)
        if (item != null) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): NewsProperty {
        return items[position]
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == items.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    class ViewHolder(itemView: View) : BaseViewHolder(itemView){
        override fun bind(
            item: NewsProperty,
            listener : View.OnClickListener,
            position: Int
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
            fun from(parent: ViewGroup): BaseViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_news, parent, false)
                return ViewHolder(
                    view
                )
            }
        }

        override fun clear() {

        }
    }

    private fun createOnClickListener(newsProperty: NewsProperty, position: Int): View.OnClickListener {
        return View.OnClickListener {
            val newsListContainer = if (myNewsListContainer== NewsListContainer.TOPHEADLINES)
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


    class ProgressHolder internal constructor(itemView: View?) :
        BaseViewHolder(itemView!!) {
        init {
        }

        override fun clear() {

        }
    }
}