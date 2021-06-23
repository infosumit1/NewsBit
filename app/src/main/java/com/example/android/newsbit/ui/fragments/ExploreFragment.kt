package com.example.android.newsbit.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.newsbit.R
import com.example.android.newsbit.adapters.CategoryAdapter
import com.example.android.newsbit.adapters.NewsSourceAdapter
import com.example.android.newsbit.models.Category
import com.example.android.newsbit.models.NewsSource
import com.example.android.newsbit.ui.MainActivity
import com.example.android.newsbit.ui.NewsViewModel
import java.util.*


class ExploreFragment : Fragment(R.layout.fragment_explore) {

    lateinit var viewModel: NewsViewModel
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var categoryItemView: RecyclerView
    lateinit var newsSourceAdapter: NewsSourceAdapter
    lateinit var newsSourceItemView : RecyclerView
    var categories: MutableList<Category> = mutableListOf()
    var newsSources: MutableList<NewsSource> = mutableListOf()
    val date = Calendar.getInstance()
    var previous=date.add(Calendar.DAY_OF_MONTH, -1); //Goes to previous day
    val year = date.get(Calendar.YEAR)
    val month = date.get(Calendar.MONTH)+1
    val day = date.get(Calendar.DAY_OF_MONTH)
    var from = "$year-$month-$day"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        categories = mutableListOf(
            Category("Business", R.drawable.business,false),
            Category("Covid", R.drawable.covid,true),
            Category("Entertainment", R.drawable.entertainment,false),
            Category("Health", R.drawable.health,false),
            Category("International", R.drawable.international,true),
            Category("Politics", R.drawable.politics,true),
            Category("Science", R.drawable.science,false),
            Category("Sports", R.drawable.sports,false),
            Category("Technology", R.drawable.technology,false)
        )

        newsSources  = mutableListOf(
            NewsSource("bbc-news","BBC",R.drawable.sports),
            NewsSource("the-hindu","The Hindu",R.drawable.the_hindu),
            NewsSource("the-times-of-india","Times Of India",R.drawable.technology)
        )
    }

    /*https://stackoverflow.com/questions/14678593/the-application-may-be-doing-too-much-work-on-
    its-main-thread*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryItemView = view.findViewById(R.id.categoryItemView)
        newsSourceItemView = view.findViewById(R.id.newsSourceItemView)


        setupCategoryRecyclerView()
        setupNewsSourceRecyclerView()

        categoryAdapter.setOnItemClickListener {

            if(it.isCustomCategory==false)
            { viewModel.getCategoryNews("in", it.categoryName, 1) }
            else
            {

                if(it.categoryName=="Covid")
                {
                    viewModel.getCustomCategoryNews("\"Covid\"+India","en",from,1)
                }
                else if(it.categoryName=="Politics"){
                    viewModel.getCustomCategoryNews("\"Politics\"+India","en",from,1)
                }
                else if(it.categoryName=="International"){
                    viewModel.getCustomCategoryNews("International","en",from,1)
                }
            }

            val bundle = Bundle().apply {
                putString("categoryName", it.categoryName)
                putBoolean("isCustomCategory",it.isCustomCategory)
            }
            findNavController().navigate(
                R.id.action_exploreFragment_to_categoryNewsFragment,
                bundle
            )
        }

        newsSourceAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString("newsSourceId", it.newsSourceID)

            }
            findNavController().navigate(
                R.id.action_exploreFragment_to_sourceNewsFragment,
                bundle
            )
        }

    }

    private fun setupCategoryRecyclerView() {
        categoryAdapter = CategoryAdapter(categories.toList())
        categoryItemView.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(activity, 3)
        }
    }

    private fun setupNewsSourceRecyclerView() {
        newsSourceAdapter = NewsSourceAdapter(newsSources.toList())
        newsSourceItemView.apply {
            adapter = newsSourceAdapter
            layoutManager = GridLayoutManager(activity, 3)
        }
    }

}