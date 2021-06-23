package com.example.android.newsbit.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.newsbit.R
import com.example.android.newsbit.adapters.NewsAdapter
import com.example.android.newsbit.models.Article
import com.example.android.newsbit.ui.MainActivity
import com.example.android.newsbit.ui.NewsViewModel
import com.example.android.newsbit.utils.Resource
import kotlinx.coroutines.delay
import java.util.*


class CategoryNewsFragment : Fragment(R.layout.fragment_category_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var categoryNewsAdapter: NewsAdapter
    lateinit var categoryNewsItemView: RecyclerView
    lateinit var paginationProgressBarView: ProgressBar

    var totalResults = 0
    var isLoading = false
    var isScrolling = false
    val categoryArgs: CategoryNewsFragmentArgs by navArgs()
    var category = ""
    var isCustomCategory = false
    val TAG = "CategoryNewsFragment"
    var categoryNewsPageTemp = 1
    var customCategoryNewsPageTemp = 1

    val date = Calendar.getInstance()
    var previous=date.add(Calendar.DAY_OF_MONTH, -1); //Goes to previous day
    val year = date.get(Calendar.YEAR)
    val month = date.get(Calendar.MONTH)+1
    val day = date.get(Calendar.DAY_OF_MONTH)
    var from = "$year-$month-$day"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        category = categoryArgs.categoryName
        isCustomCategory = categoryArgs.isCustomCategory

        Log.e(TAG,from)

        /*if(isCustomCategory==false)
        { viewModel.getCategoryNews("in", category, categoryNewsPageTemp) }
        else
        {
            Log.e(TAG,"Inside Check if Custom Category")
            if(category=="Covid")
            {
                viewModel.getCustomCategoryNews("\"Covid\"+India","en",from,customCategoryNewsPageTemp)
            }
            else if(category=="Politics"){
                viewModel.getCustomCategoryNews("\"Politics\"+India","en",from,customCategoryNewsPageTemp)
            }
            else if(category=="International"){
                viewModel.getCustomCategoryNews("International","en",from,customCategoryNewsPageTemp)
            }
        }*/
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryNewsItemView = view.findViewById(R.id.categoryNewsItemView)
        paginationProgressBarView = view.findViewById(R.id.categoryNewsPaginationProgressBar)

        setupRecyclerView()

        categoryNewsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_categoryNewsFragment_to_articleFragment,
                bundle
            )
        }



        /*Log.e(TAG,"----------------------------------------------------------------------------")
        Log.e(TAG,category)*/

        /*Log.e(TAG,"request made for category news")*/

        /* if(category!=lastCategoryCalled) {
             viewModel.getCategoryNews("in", category, categoryNewsPageTemp)
             lastCategoryCalled=category // to manage same category clicks
         }*/

        viewModel.categoryNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { categoryNewsResponse ->
                        /*Log.e(TAG, categoryNewsResponse.articles.size.toString())*/
                        categoryNewsAdapter.differ.submitList(categoryNewsResponse.articles.toList())
                        totalResults = categoryNewsResponse.totalResults
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                        showProgressBar()
                }
            }
        })

    }

    private fun hideProgressBar() {
        paginationProgressBarView.visibility = View.INVISIBLE
        isLoading = false //use for paging
    }

    private fun showProgressBar() {
        paginationProgressBarView.visibility = View.VISIBLE
        isLoading = true //use for paging
    }

    val categoryNewsScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

            val currentItemCount = layoutManager.itemCount
            val isCurrentItemCountIsLessThan60 = (currentItemCount < 60)
            val shouldPaginate = totalResults > currentItemCount &&
                    firstVisiblePosition >= currentItemCount - 2 &&
                    isScrolling && !isLoading && isCurrentItemCountIsLessThan60

            if (shouldPaginate == true)  {
                if(isCustomCategory==false)
                {viewModel.getCategoryNews("in", category, ++categoryNewsPageTemp)}
                else
                {
                    if(category=="Covid")
                    {
                        viewModel.getCustomCategoryNews("\"Covid\"+India","en",from,++customCategoryNewsPageTemp)
                    }
                    else if(category=="Politics"){
                        viewModel.getCustomCategoryNews("\"Politics\"+India","en",from,++customCategoryNewsPageTemp)
                    }
                    else if(category=="International"){
                        viewModel.getCustomCategoryNews("International","en",from,++customCategoryNewsPageTemp)
                    }
                }
                    isScrolling = false
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        categoryNewsAdapter = NewsAdapter()
        categoryNewsItemView.apply {
            adapter = categoryNewsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(categoryNewsScrollListener)
        }
    }

}
