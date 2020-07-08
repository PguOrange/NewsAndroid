package com.example.newsandroid.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.newsandroid.domain.NewsProperty
import java.text.FieldPosition

abstract class BaseViewHolder(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!) {
    var currentPosition = 0
        private set

    protected abstract fun clear()
    open fun bind(item: NewsProperty,
                  listener : View.OnClickListener,
                    position: Int) {
        currentPosition = position
        clear()
    }

}