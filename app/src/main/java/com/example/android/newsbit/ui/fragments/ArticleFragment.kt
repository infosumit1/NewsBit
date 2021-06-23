package com.example.android.newsbit.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.newsbit.R
import com.example.android.newsbit.models.Article
import com.example.android.newsbit.ui.MainActivity
import com.example.android.newsbit.ui.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    val articleArgs: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article: Article = articleArgs.article
        val webView: WebView = view.findViewById (R.id.webView)
        val fab: FloatingActionButton= view.findViewById(R.id.fab)

        val webSettings: WebSettings = webView.getSettings()
        webSettings.javaScriptEnabled = true

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        lateinit var savedArticles: List<Article>

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            savedArticles=articles
        })

        fab.setOnClickListener {
            //TODO: find out how to write code to prevent duplicate entry

            /*
            * Following may not be a good way. I am checking if article is already saved. if not
            * only then insert otherwise not. I could not figure out how to define schema and use
            * onConflictReplace to prevent duplicates. I will try it when I complete other
            * functionalites of this app. Till then I will use below code.
            *
            * Also one more
            * VERY IMPORTANT NOTE:
            * Handle null case in article because some article violate null constraint and crash
            * the app. so handle case when some of the response is null like no image, author etc.s
            */

            var alreadySaved  = false

            for(savedArticle in savedArticles)
            {
                if(savedArticle.url==article.url) alreadySaved=true
            }
            if (alreadySaved==false)
            {
                viewModel.saveArticle(article)
            }
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }
}