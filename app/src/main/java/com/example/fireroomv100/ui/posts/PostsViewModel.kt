package com.example.fireroomv100.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fireroomv100.model.Entry
import java.lang.Exception
import java.util.Date

class PostsViewModel : ViewModel() {

    private val postsList:MutableList<Entry> = mutableListOf()

    private val _posts = MutableLiveData<MutableList<Entry>>()
    val posts: LiveData<MutableList<Entry>> = _posts

    fun createPost(author:String,text:String,imageUri:String?):Entry{
        if(text.isEmpty()) throw Exception("Text cannot be empty")
        val date=Date()
        val post=Entry.Post(text,author,imageUri,date.toString())
        postsList.add(post)
        _posts.postValue(postsList)
        return post
    }
}