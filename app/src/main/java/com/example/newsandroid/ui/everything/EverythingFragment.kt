package com.example.newsandroid.ui.everything

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsandroid.R
import com.example.newsandroid.adapter.NewsAdapter
import com.example.newsandroid.enums.NewsApiStatus
import com.example.newsandroid.factory.ViewModelFactory
import com.example.newsandroid.ui.topheadlines.TopHeadlinesViewModel
import kotlinx.android.synthetic.main.everything_fragment.*
import kotlinx.android.synthetic.main.top_headlines_fragment.*

class EverythingFragment : Fragment() {

    lateinit var adapter: NewsAdapter

    private val everythingViewModel: EverythingViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            ViewModelFactory(application)
        ViewModelProviders.of(
            this, viewModelFactory).get(EverythingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.everything_fragment, container, false)

        everythingViewModel.status.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it) {
                    NewsApiStatus.LOADING -> {
                        status_image_everything.visibility = View.VISIBLE
                        status_image_everything.setImageResource(R.drawable.loading_animation)
                    }
                    NewsApiStatus.ERROR -> {
                        status_image_everything.visibility = View.VISIBLE
                        status_image_everything.setImageResource(R.drawable.ic_connection_error)
                        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.ERROR_API -> {
                        status_image_everything.visibility = View.VISIBLE
                        status_image_everything.setImageResource(R.drawable.ic_connection_error)
                        Toast.makeText(activity, "Error to get news", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.ERROR_WITH_CACHE  -> {
                        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.ERROR_API_WITH_CACHE -> {
                        Toast.makeText(activity, "Error to get news", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.DONE -> {
                        status_image_everything.visibility = View.GONE
                    }
                }
            }
        })

        everythingViewModel.property.observe(viewLifecycleOwner, Observer {
            everything_news_list.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            adapter = NewsAdapter(it)
            everything_news_list.adapter = adapter
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh_layout_everything.setOnRefreshListener {
            everythingViewModel.getEverythingProperties()
            swipe_refresh_layout_everything.isRefreshing = false
        }
    }

}