package com.example.newsandroid.ui.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsandroid.R
import com.example.newsandroid.factory.ViewModelFactory
import com.example.newsandroid.ui.topheadlines.TopHeadlinesViewModel
import kotlinx.android.synthetic.main.activity_main.*

class DetailNewsFragment : Fragment() {

    private val detailNewsViewModel: DetailNewsViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            ViewModelFactory(application)
        ViewModelProviders.of(
            this, viewModelFactory).get(DetailNewsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.detail_news_fragment, container, false)

        return root
    }

}