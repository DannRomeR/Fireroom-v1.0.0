package com.example.fireroomv100.ui.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fireroomv100.databinding.CreatePostBinding
import com.example.fireroomv100.databinding.RowPostBinding
import com.example.fireroomv100.model.Entry

/**
 * @author Daniel Mendoza
 * Recycler adapter to display the user's posts
 */
class PostAdapter:RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    var items: List<Entry>?=null

    /**
     * @author Daniel Mendoza
     * ViewHolder class for each post
     */
    class PostViewHolder(val binding: RowPostBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(post:Entry){
            binding.post=post
        }
    }

    /**
     * Creates a row with an empty view to display the content of a Post
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding=RowPostBinding.inflate(inflater,parent,false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items?.size?:0
    }

    /**
     * Feeds the ViewHolder with a Post
     */
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item=items?.get(position)
        if(item!=null) holder.bind(item)
    }
}