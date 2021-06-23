package com.example.android.newsbit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.newsbit.R
import com.example.android.newsbit.models.Category

class CategoryAdapter(val categories: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImageView: ImageView = itemView.findViewById(R.id.category_icon)
        val categoryNameView: TextView = itemView.findViewById(R.id.category_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_category,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    private var onItemClickListener: ((Category) -> Unit)? = null


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentTopic = categories[position]
        holder.categoryNameView.text = currentTopic.categoryName
        holder.categoryImageView.setImageResource(currentTopic.imageId)

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(currentTopic)
            }}

    }

    fun setOnItemClickListener(listener: (Category) -> Unit) {
        onItemClickListener = listener
    }


}