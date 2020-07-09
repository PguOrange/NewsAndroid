package com.example.newsandroid.ui.topheadlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsandroid.MainActivity
import com.example.newsandroid.R
import com.example.newsandroid.adapter.NewsAdapter
import com.example.newsandroid.enums.NewsListContainer
import com.example.newsandroid.enums.NewsApiStatus
import com.example.newsandroid.factory.ViewModelFactory
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.top_headlines_fragment.*


class TopHeadlinesFragment : Fragment() {

    lateinit var adapter: NewsAdapter


    private val topHeadlinesViewModel: TopHeadlinesViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            ViewModelFactory(application)
        ViewModelProviders.of(
            this, viewModelFactory).get(TopHeadlinesViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.top_headlines_fragment, container, false)

        topHeadlinesViewModel.status.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it) {
                    NewsApiStatus.LOADING -> {
                        status_image.visibility = View.VISIBLE
                        status_image.setImageResource(R.drawable.loading_animation)
                    }
                    NewsApiStatus.ERROR -> {
                        status_image.visibility = View.VISIBLE
                        status_image.setImageResource(R.drawable.ic_connection_error)
                        filter.visibility = View.GONE
                        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.ERROR_API -> {
                        status_image.visibility = View.VISIBLE
                        status_image.setImageResource(R.drawable.ic_connection_error)
                        filter.visibility = View.GONE
                        Toast.makeText(activity, "Error to get news", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.ERROR_WITH_CACHE -> {
                        status_image.visibility = View.GONE
                        filter.visibility = View.GONE
                        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.ERROR_API_WITH_CACHE -> {
                        status_image.visibility = View.GONE
                        filter.visibility = View.GONE
                        Toast.makeText(activity, "Error to get news", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.DONE -> {
                        status_image.visibility = View.GONE
                        filter.visibility = View.VISIBLE
                        text_category.text = topHeadlinesViewModel.currentCategory
                    }
                }
            }
        })


        topHeadlinesViewModel.categoryList.observe(viewLifecycleOwner, object :Observer<List<String>> {
            override fun onChanged(data: List<String>?) {
                data ?: return
                val chipGroup = category_list
                val inflator = LayoutInflater.from(chipGroup.context)
                data.map { categoryName ->
                    val chip = inflator.inflate(R.layout.category, chipGroup, false) as Chip
                    chip.text = categoryName
                    chip.tag = categoryName
                    chipGroup.addView(chip)
                    if (categoryName == topHeadlinesViewModel.currentCategory) chip.isChecked = true
                    chip.setOnCheckedChangeListener { button, isChecked ->
                        topHeadlinesViewModel.onFilterChanged(button.tag as String, isChecked)
                    }

                }
            }
        })

        topHeadlinesViewModel.property.observe(viewLifecycleOwner, Observer {
            news_list.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            val adapter = NewsAdapter(it, NewsListContainer.TOPHEADLINES)
            news_list.apply {
                this.adapter = adapter
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }

        })

        (activity as MainActivity?)!!.setFragmentRefreshListener(object :
            MainActivity.FragmentRefreshListener {
            override fun onRefresh() {

                topHeadlinesViewModel.getTopHeadlinesProperties()
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh_recycler.setOnRefreshListener {
            topHeadlinesViewModel.getTopHeadlinesProperties()
            swipe_refresh_recycler.isRefreshing = false
        }
    }

}