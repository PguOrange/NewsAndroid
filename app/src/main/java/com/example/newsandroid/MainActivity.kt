package com.example.newsandroid

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.newsandroid.ui.detail.DetailNewsFragment
import com.example.newsandroid.ui.topheadlines.TopHeadlinesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailNewsFragment -> {
                    bottom_navigation?.visibility = View.GONE
                }
                else -> bottom_navigation?.visibility = View.VISIBLE
            }
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.topHeadlinesFragment -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.topHeadlinesFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.everythingFragment -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.everythingFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
}