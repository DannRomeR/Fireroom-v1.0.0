package com.example.fireroomv100.ui.posts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fireroomv100.databinding.PostsBinding
import com.example.fireroomv100.model.Entry
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class PostsFragment : Fragment() {

    private var _binding: PostsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
    private lateinit var postsViewModel:PostsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        postsViewModel=ViewModelProvider(this)[PostsViewModel::class.java]
        _binding = PostsBinding.inflate(inflater, container, false)
        binding?.let { initUI(it) }
        /*val textView: TextView = binding.textHome
        postsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return binding!!.root
    }

    private fun initUI(view: PostsBinding) {
        view.createPostFab.setOnClickListener { showNewPostDialog() }
        val adapter=PostAdapter()

        postsViewModel.posts.observe(viewLifecycleOwner){
            adapter.items=it
            adapter.notifyDataSetChanged()
        }
        view.listPostRecyclerView.adapter=adapter
    }

    private fun showNewPostDialog() {
        PostDialogBuilder(requireContext())
            .setOnDataInputListener { input, imageUri ->
                createPost(input, imageUri)
            }
            .build()
            .show()
    }

    private fun createPost(text: String, imageUri: String?) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            try{
                val post=postsViewModel.createPost(user.displayName?:"Anon",text,imageUri)
            }catch (e:Exception){
                Log.e("PostFragment",e.stackTraceToString())
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}