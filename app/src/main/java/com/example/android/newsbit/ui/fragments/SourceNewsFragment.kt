package com.example.android.newsbit.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.newsbit.R
import com.example.android.newsbit.adapters.NewsAdapter
import com.example.android.newsbit.ui.MainActivity
import com.example.android.newsbit.ui.NewsViewModel
import com.example.android.newsbit.utils.Resource
import java.util.*


class SourceNewsFragment : Fragment(R.layout.fragment_source_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var sourceNewsAdapter: NewsAdapter
    lateinit var sourceNewsItemView: RecyclerView
    lateinit var paginationProgressBarView: ProgressBar
    var totalResults = 0 // totalResults will be received in news response later
    var isLoading = false
    var isScrolling = false
    val sourceNewsArgs: SourceNewsFragmentArgs by navArgs()
    var sourceId = "the-hindu"
    val TAG = "SourceNewsFragment"
    var sourceNewsPageTemp = 1

    val date = Calendar.getInstance()
    var previous=date.add(Calendar.DAY_OF_MONTH, -1); //Goes to previous day
    val year = date.get(Calendar.YEAR)
    val month = date.get(Calendar.MONTH)+1
    val day = date.get(Calendar.DAY_OF_MONTH)
    var from = "$day-$month-$year"




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        sourceId = sourceNewsArgs.newsSourceId

        Log.e(TAG,sourceId)

        viewModel.getSourceNews(sourceId,"en",from,sourceNewsPageTemp)

        sourceNewsItemView = view.findViewById(R.id.sourceNewsItemView)
        paginationProgressBarView = view.findViewById(R.id.sourceNewsPaginationProgressBar)

        setupRecyclerView()

        sourceNewsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_sourceNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.sourceNews.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { sourceNewsResponse ->

                        sourceNewsAdapter.differ.submitList(sourceNewsResponse.articles.toList())
                        totalResults = sourceNewsResponse.totalResults
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

    val sourceNewsScrollListener = object : RecyclerView.OnScrollListener() {
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

                viewModel.getSourceNews(sourceId,"en",from,++sourceNewsPageTemp)

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
        sourceNewsAdapter = NewsAdapter()
        sourceNewsItemView.apply {
            adapter = sourceNewsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(sourceNewsScrollListener)
        }
    }
}