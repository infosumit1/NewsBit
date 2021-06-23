package com.example.android.newsbit.models

import java.io.Serializable

data class Source(
    val id: Any,
    val name: String
): Serializable

/*
* made this serializable because if it is not serializable then it causes error on pressing home
* button (and maybe at other places too)
* I don't exactly remember this but it is because it is linked directly to Article
* model class and according to concept of serialization(which I will study more about)
* it must be serializable too. (I have read about how connected object should be serializable and
* if you don't want them to be serializable then some keyword is used which I don't remember
* currently)
* */