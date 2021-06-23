package com.example.android.newsbit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.newsbit.R
import com.example.android.newsbit.databinding.ActivityMainBinding
import com.example.android.newsbit.db.ArticleDatabase
import com.example.android.newsbit.repository.NewsRepository
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    val TAG = "Main Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        The next 3 lines(where we are instantiating viewModel) work only
        if we write them before setContentView(R.layout.activity_main).
        It has something to relate with lifecycle. I don't understand it fully yet
        because in phillip lackner course it works fine if we write these lines after
        setContentView(R.layout.activity_main) line
         */

        val newsRepository=NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        setTheme(R.style.Theme_NewsBit)
        setContentView(R.layout.activity_main)

       /* Log.e(TAG, "\n\n Ho to raha hai yaar \n\n")*/



        if(this::viewModel.isInitialized)
        {
            Log.e(TAG, " Ho to raha hai yaar ")
        }
        else
        {
            Log.e(TAG, "Abhi nahi yaar ")
        }

        val navController = findNavController(R.id.newsNavHostFragment)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
    }
}