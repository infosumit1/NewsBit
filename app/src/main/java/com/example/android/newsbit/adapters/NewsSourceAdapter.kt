package com.example.android.newsbit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.newsbit.R
import com.example.android.newsbit.models.Category
import com.example.android.newsbit.models.NewsSource


class NewsSourceAdapter(val newsSources: List<NewsSource>) :
    RecyclerView.Adapter<NewsSourceAdapter.NewsSourceViewHolder>() {

    inner class NewsSourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsSourceImageView: ImageView = itemView.findViewById(R.id.category_icon)
        val newsSourceNameView: TextView = itemView.findViewById(R.id.category_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSourceViewHolder {
        return NewsSourceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_category,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return newsSources.size
    }

    private var onItemClickListener: ((NewsSource) -> Unit)? = null

    override fun onBindViewHolder(holder: NewsSourceViewHolder, position: Int) {

        val currentNewsSource = newsSources[position]
        holder.newsSourceNameView.text = currentNewsSource.newsSourceName
        holder.newsSourceImageView.setImageResource(currentNewsSource.imageId)

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(currentNewsSource)
            }}

    }



    fun setOnItemClickListener(listener: (NewsSource) -> Unit) {
        onItemClickListener = listener
    }


}

