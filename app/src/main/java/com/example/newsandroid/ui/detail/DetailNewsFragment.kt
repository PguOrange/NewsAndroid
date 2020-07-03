package com.example.newsandroid.ui.detail

import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.transition.ChangeBounds
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.transition.TransitionInflater
import com.example.newsandroid.R
import com.example.newsandroid.factory.ViewModelFactory
import com.example.newsandroid.ui.topheadlines.TopHeadlinesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.detail_news_fragment.*

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

        sharedElementEnterTransition = TransitionInflater.from(this.context).inflateTransition(R.transition.change_bounds)
        sharedElementReturnTransition =  TransitionInflater.from(this.context).inflateTransition(R.transition.change_bounds)
        val root = inflater.inflate(R.layout.detail_news_fragment, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = DetailNewsFragmentArgs.fromBundle(arguments!!)
        val news = args.news
        ViewCompat.setTransitionName(news_title_detail, "title_"+args.position)
        ViewCompat.setTransitionName(news_author_detail, "author_"+args.position)
        ViewCompat.setTransitionName(news_description_detail, "description_"+args.position)
        news_title_detail.text = news.title
        news_author_detail.text = news.author
        news_description_detail.text = news.description
    }

}