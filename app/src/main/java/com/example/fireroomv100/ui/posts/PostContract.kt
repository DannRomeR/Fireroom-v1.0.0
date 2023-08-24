package com.example.fireroomv100.ui.posts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.example.fireroomv100.model.Entry
import java.lang.Exception
import java.util.Date

/**
 * @author Daniel Mendoza
 * class to launch the CreatePostActivity and parse the result into an Entry
 * */
class PostContract:ActivityResultContract<Pair<String,String>,Entry?>() {

    override fun createIntent(context: Context, input: Pair<String, String>): Intent {
        val bundle= Bundle().apply{
            putString(CreatePostActivity.AUTHOR,input.first)
            putString(CreatePostActivity.PROFILE_PIC,input.second)
        }
        val intent=Intent(context,CreatePostActivity::class.java)
        intent.putExtras(bundle)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Entry? {
        if(resultCode==Activity.RESULT_OK && intent!=null){
            val data=intent.extras
            if(data!=null){
                val message=data.getString(CreatePostActivity.MESSAGE)
                val imageUrl=data.getString(CreatePostActivity.IMAGE_URL)
                val author=data.getString(CreatePostActivity.AUTHOR)
                if(author!=null&&message!=null){
                    return createPost(author,message,imageUrl)
                }
            }
        }
        return null
    }

    private fun createPost(author: String, text: String, imageUri: String?): Entry {
        if (text.isEmpty()&&imageUri.isNullOrEmpty()) throw Exception("Text or image cannot be empty")
        val date = Date()
        return Entry.Post(text, author, imageUri, date.toString())
    }
}
