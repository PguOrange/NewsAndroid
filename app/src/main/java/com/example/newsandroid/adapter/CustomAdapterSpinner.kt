package com.example.newsandroid.adapter
import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


class CustomAdapterSpinner(internal var context: Context, internal var flags: Array<Int>, internal var Network: Array<String>) : BaseAdapter() { internal var inflter: LayoutInflater
    init {
        inflter = LayoutInflater.from(context)
    }
    override fun getCount(): Int {
        return flags.size
    }
    override fun getItem(i: Int): Any? {
        return null
    }
    override fun getItemId(i: Int): Long {
        return 0
    }
    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = inflter.inflate(com.example.newsandroid.R.layout.spinner_topheadlines_layout, null)
        val icon = view.findViewById(com.example.newsandroid.R.id.spinner_imageView) as ImageView
        val names = view.findViewById(com.example.newsandroid.R.id.spinner_textView) as TextView
        icon.setImageResource(flags[i])
        names.text = Network[i]
        return view
    }
}