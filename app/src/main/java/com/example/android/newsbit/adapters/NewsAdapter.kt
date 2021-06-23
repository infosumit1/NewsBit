package com.example.android.newsbit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.newsbit.R
import com.example.android.newsbit.models.Article

class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleView: TextView = itemView.findViewById(R.id.title)
        val descriptionView: TextView = itemView.findViewById(R.id.description)
        val sourceView: TextView = itemView.findViewById(R.id.source_name)
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val bookmarkBtn: Button = itemView.findViewById(R.id.button)

    }


    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {

        /* val view=LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        val viewHolder= NewsViewHolder(view)
        view.setOnClickListener() {
            listener.onItemClicked(differ.currentList[viewHolder.position])
        }
        view.findViewById<TextView>(R.id.description).setOnClickListener()
        {
            listener.onDescriptionClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder*/

        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_news,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null
    private var onBookmarkClickListener: ((Article) -> Unit)? = null


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.itemView.apply {

            holder.titleView.text = currentItem.title
            holder.descriptionView.text = currentItem.description
            holder.sourceView.text = currentItem.source.name

            Glide.with(holder.itemView.context).load(currentItem.urlToImage).into(holder.imageView)

            holder.itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(currentItem)
                }
            }

            holder.bookmarkBtn.setOnClickListener {
                onBookmarkClickListener?.let {
                    it(currentItem)
                }
            }

        }
    }

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnBookmarkClickListener(listener: (Article) -> Unit) {
        onBookmarkClickListener = listener
    }


}



