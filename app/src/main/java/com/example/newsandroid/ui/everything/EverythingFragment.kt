package com.example.newsandroid.ui.everything

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsandroid.R
import com.example.newsandroid.adapter.NewsAdapter
import com.example.newsandroid.enums.NewsApiStatus
import com.example.newsandroid.factory.ViewModelFactory
import com.example.newsandroid.util.transformSpinnerStringToParametersApi
import kotlinx.android.synthetic.main.everything_fragment.*
import kotlinx.android.synthetic.main.layout_custom_dialog.*
import kotlinx.android.synthetic.main.layout_custom_dialog.view.*


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
            everythingViewModel.getEverythingProperties(everythingViewModel.currentLanguage,everythingViewModel.currentSort)
            swipe_refresh_layout_everything.isRefreshing = false
        }

        filter_button_everything.setOnClickListener() {
            showDialog()
        }

    }

    private fun showDialog() {
        val alert = AlertDialog.Builder(context)
        val alertLayout: View = layoutInflater.inflate(R.layout.layout_custom_dialog, null)
        alert.setTitle("Filtre de recherche")
        alert.setView(alertLayout)
        alert.setCancelable(false)
        alert.setNegativeButton(
            "Cancel"
        ) { dialog, which ->
        }

        alert.setPositiveButton(
            "OK"
        ) { dialog, which ->
            val language = alertLayout.sp_language.selectedItem.toString()
            val sort = alertLayout.sp_sort.selectedItem.toString()
            everythingViewModel.getEverythingProperties(language, transformSpinnerStringToParametersApi(sort))
        }
        val dialog = alert.create()
        dialog.show()
    }

}