package com.example.newsandroid.ui.topheadlines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsandroid.R
import com.example.newsandroid.enums.Category
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.top_headlines_fragment.*


class TopHeadlinesFragment : Fragment() {

    lateinit var adapter: TopHeadlinesAdapter

    private val topHeadlinesViewModel: TopHeadlinesViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = TopHeadlinesViewModelFactory(application)
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
                        country_button.visibility = View.GONE
                        swipe_refresh_recycler.isEnabled = false
                        text_category.visibility = View.GONE
                        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.ERRORWITHCACHE -> {
                        country_button.visibility = View.GONE
                        swipe_refresh_recycler.isEnabled = false
                        text_category.visibility = View.GONE
                        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.ERRORWITHCACHEANDFILTERDISPLAYED -> {
                        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.DONE -> {
                        status_image.visibility = View.GONE
                        swipe_refresh_layout.isEnabled = false
                        swipe_refresh_layout.isRefreshing = false
                        swipe_refresh_recycler.isEnabled = true
                        country_button.visibility = View.VISIBLE
                        country_button.text = topHeadlinesViewModel.currentCountry
                        text_category.visibility = View.VISIBLE
                        text_category.text = topHeadlinesViewModel.textCategory
                    }
                }
            }
        })


        topHeadlinesViewModel.categoryList.observe(viewLifecycleOwner, object :Observer<List<String>> {
            override fun onChanged(data: List<String>?) {
                data ?: return
                val chipGroup = category_list
                val inflator = LayoutInflater.from(chipGroup.context)
                val children = data.map { categoryName ->
                    val chip = inflator.inflate(R.layout.category, chipGroup, false) as Chip
                    chip.text = categoryName
                    chip.tag = categoryName
                    chip.setOnCheckedChangeListener { button, isChecked ->
                        topHeadlinesViewModel.onFilterChanged(button.tag as String, isChecked)
                    }
                    chip
                }
                chipGroup.removeAllViews()

                for (chip in children) {
                    chipGroup.addView(chip)
                }
            }
        })

        topHeadlinesViewModel.property.observe(viewLifecycleOwner, Observer {
            news_list.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            adapter = TopHeadlinesAdapter(it)
            news_list.adapter = adapter
        })


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh_layout.setOnRefreshListener {
            topHeadlinesViewModel.getTopHeadlinesProperties()
            swipe_refresh_layout.isRefreshing = false
        }

        country_button.setOnClickListener() {
            topHeadlinesViewModel.changeCurrentCountry()
            topHeadlinesViewModel.onCountryChanged()
            country_button.text = topHeadlinesViewModel.currentCountry
        }

        swipe_refresh_recycler.setOnRefreshListener {
            topHeadlinesViewModel.onListRefreshed()
            swipe_refresh_recycler.isRefreshing = false
        }
    }

}