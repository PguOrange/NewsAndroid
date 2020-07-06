package com.example.newsandroid.ui.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.transition.ChangeBounds
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.transition.TransitionInflater
import com.example.newsandroid.R
import com.example.newsandroid.domain.NewsProperty
import com.example.newsandroid.factory.ViewModelFactory
import com.example.newsandroid.ui.topheadlines.TopHeadlinesViewModel
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.detail_news_fragment.*
import kotlinx.android.synthetic.main.everything_fragment.*
import kotlinx.android.synthetic.main.top_headlines_fragment.*

class DetailNewsFragment : Fragment() {


    private val detailNewsViewModel: DetailNewsViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val viewModelFactory =
            ViewModelFactory(application)
        ViewModelProviders.of(
            this, viewModelFactory
        ).get(DetailNewsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedElementEnterTransition =
            TransitionInflater.from(this.context).inflateTransition(R.transition.change_bounds)
        sharedElementReturnTransition =
            TransitionInflater.from(this.context).inflateTransition(R.transition.change_bounds)
        val root = inflater.inflate(R.layout.detail_news_fragment, container, false)

        detailNewsViewModel.news.observe(viewLifecycleOwner, Observer{
            news_title_detail.text = it.title
            news_author_detail.text = it.author
            news_description_detail.text = it.description
            news_date_detail.text = it.publishedAt
            news_content_detail.text = it.content

            if (it.url.isNotEmpty())
            {
                Picasso.with(context).load(detailNewsViewModel.uriImage).into(news_image_detail)
                Picasso.with(context).load(detailNewsViewModel.uriImage).into(news_image_content_detail)
            }
            else
            {
                Picasso.with(context).load(R.drawable.no_image).into(news_image_detail)
                Picasso.with(context).load(R.drawable.no_image).into(news_image_content_detail)
            }

            news_button_url_detail.setOnClickListener{
                val urlString = detailNewsViewModel.uri
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setPackage("com.android.chrome")
                try {
                    context?.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    intent.setPackage(null)
                    context?.startActivity(intent)
                }
            }

        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = DetailNewsFragmentArgs.fromBundle(arguments!!)
        detailNewsViewModel.getNewsDetail(args.news)
        ViewCompat.setTransitionName(news_title_detail, "title_"+args.position)
        ViewCompat.setTransitionName(news_author_detail, "author_"+args.position)
        ViewCompat.setTransitionName(news_description_detail, "description_"+args.position)

    }

}