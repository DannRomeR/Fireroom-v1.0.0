package com.example.fireroomv100.ui.posts

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.fireroomv100.R
import com.example.fireroomv100.databinding.NewPostBinding

/**
 * @author Daniel Mendoza
 * Activity to create or edit a post
 *
 */
class CreatePostActivity : AppCompatActivity() {
    companion object{
        const val  IMAGE_URL="IMAGE_URL"
        const val MESSAGE="MESSAGE"
        const val PROFILE_PIC="PROFILE_PIC"
        const val AUTHOR="AUTHOR"
    }

    lateinit var binding:NewPostBinding
    private var imageUrl:String=""
    private var message:String=""
    private var author:String=""
    private var profilePic:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        setupUI()
    }

    private fun setupActionBar(){
        supportActionBar?.apply {
            title=getString(R.string.add_post)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupUI(){
        unpackParams()
        binding.editorNameTextView.text=author
        if(imageUrl.isNotEmpty()) binding.editorPicImageView.load(imageUrl)
        binding.textPostEditText.requestFocus()
        binding.cameraOptionButton.setOnClickListener { takePhoto() }
        binding.picOptionButton.setOnClickListener { pickImage() }
        binding.sendOptionButton.setOnClickListener{
            onPostConfirmed()
        }
    }

    private fun unpackParams() {
        val bundle=intent.extras
        bundle?.let{
            author=it.getString(AUTHOR)?:""
            profilePic=it.getString(PROFILE_PIC)?:""
        }
    }

    private fun onPostConfirmed() {
        message=binding.textPostEditText.text.toString()
        if(message.isNotEmpty()||imageUrl.isNotEmpty()){
            notifyResult(message,imageUrl)
        }
        else{
            //display empty message
        }
    }

    private fun notifyResult(message: String, imageUrl: String) {
        val bundle=Bundle().apply {
            putString(IMAGE_URL,imageUrl)
            putString(MESSAGE,message)
        }
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }


    /**
     * @author Daniel Mendoza
     * Launches an image Picker activity
     * TODO: use the Photo Picker API to pick an image from the device's storage
     * @see https://developer.android.com/training/data-storage/shared/photopicker
     */
    private fun pickImage() {

    }

    /**
     * @author Daniel Mendoza
     * Launches the device's camera activity to take a picture
     * TODO: use the Camera Intent to take a picture
     * @see https://developer.android.com/training/camera/camera-intents
     */
    private fun takePhoto() {

    }


    /**
     * @author Daniel Mendoza
     * Takes the user back to the caller activity after pressing the top back arrow
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}