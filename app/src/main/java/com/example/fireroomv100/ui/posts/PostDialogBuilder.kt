package com.example.fireroomv100.ui.posts

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.fireroomv100.R
import com.example.fireroomv100.databinding.CreatePostBinding

class PostDialogBuilder(context: Context) {
    private val builder = AlertDialog.Builder(context)
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val binding = CreatePostBinding.inflate(inflater)
    private var onDataInputListener: OnDataInputListener? = null
    private var onInputCancelledListener: OnInputCancelledListener? = null

    fun setOnDataInputListener(listener: (String, String?) -> Unit): PostDialogBuilder {
        onDataInputListener = object : OnDataInputListener {
            override fun onDataInserted(input: String, imageUri: String?) {
                listener(input, imageUri)
            }
        }
        return this
    }

    fun setOnInputCancelledListener(listener: () -> Unit): PostDialogBuilder {
        onInputCancelledListener = object : OnInputCancelledListener {
            override fun onInputCancelled() {
                listener()
            }
        }
        return this
    }

    fun build():AlertDialog {
        binding.sendOptionButton.setOnClickListener {
            val inputText = binding.textPostEditText.text.toString()
            onDataInputListener?.onDataInserted(inputText, null)
        }

        builder.setTitle(R.string.add_post)
            .setView(binding.root)
            .setCancelable(true)

        val dialog=builder.create()
        binding.sendOptionButton.setOnClickListener {
            val inputText = binding.textPostEditText.text.toString()
            onDataInputListener?.onDataInserted(inputText, null)
            dialog.dismiss()
        }
        return dialog
    }

    interface OnDataInputListener {
        fun onDataInserted(input: String, imageUri: String?)
    }

    interface OnInputCancelledListener {
        fun onInputCancelled()
    }
}