package com.example.newsandroid.ui.topheadlines

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.newsandroid.R
import com.example.newsandroid.databinding.TopHeadlinesFragmentBinding

class TopHeadlinesFragment : Fragment() {


    companion object {
        fun newInstance() =
            TopHeadlinesFragment()
    }

    private lateinit var viewModel: TopHeadlinesViewModel

/*
    private val viewModel: TopHeadlinesViewModel by lazy {
        ViewModelProviders.of(this).get(TopHeadlinesViewModel::class.java)

    }
*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
/*
        val binding = TopHeadlinesFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        return binding.root
*/
        return inflater.inflate(R.layout.top_headlines_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TopHeadlinesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}