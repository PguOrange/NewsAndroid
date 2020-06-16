package com.example.newsandroid.ui.topheadlines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.newsandroid.R
import com.example.newsandroid.databinding.TopHeadlinesFragmentBinding
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.top_headlines_fragment.view.*


class TopHeadlinesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: TopHeadlinesFragmentBinding = DataBindingUtil.inflate(
        inflater, R.layout.top_headlines_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val viewModelFactory = TopHeadlinesViewModelFactory(application)

        val topHeadlinesViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(TopHeadlinesViewModel::class.java)

        binding.topHeadlinesViewModel = topHeadlinesViewModel

        val adapter = TopHeadlinesAdapter()
        binding.newsList.adapter = adapter

        topHeadlinesViewModel.status.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it) {
                    NewsApiStatus.LOADING -> {
                        binding.statusImage.visibility = View.VISIBLE
                        binding.statusImage.setImageResource(R.drawable.loading_animation)
                    }
                    NewsApiStatus.ERROR -> {
                        binding.statusImage.visibility = View.VISIBLE
                        binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                        Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
                    NewsApiStatus.DONE -> {
                        binding.statusImage.visibility = View.GONE
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
            it?.let {
                adapter.data = it
            }
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}