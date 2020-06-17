package com.example.newsandroid.ui.topheadlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsandroid.R
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.top_headlines_fragment.view.*


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
                        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.DONE -> {
                        status_image.visibility = View.GONE
                    }
                }
            }
        })


        topHeadlinesViewModel.countryList.observe(viewLifecycleOwner, object :Observer<List<String>> {
            override fun onChanged(data: List<String>?) {
                data ?: return
                val chipGroup = binding.countryList
                val inflator = LayoutInflater.from(chipGroup.context)
                val children = data.map { countryName ->
                    val chip = inflator.inflate(R.layout.country, chipGroup, false) as Chip
                    chip.text = countryName
                    chip.tag = countryName
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
    }
}