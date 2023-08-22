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

    /**
     * @author Daniel Mendoza
     * TODO: Verify if the post is not null and insert it on the DB
     * */
    fun onPostResult(post: Entry?) {
        if(post!=null){
            postsList.add(post)
            _posts.postValue(postsList)
        }
    }
}