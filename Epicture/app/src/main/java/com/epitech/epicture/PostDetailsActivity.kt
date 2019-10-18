package com.epitech.epicture

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photo_profile.view.*
import kotlinx.android.synthetic.main.post_details.*

class PostDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_details)

        /*Looking for some intent*/
        if (intent.hasExtra("image_url")  && intent.hasExtra("image_title")  && intent.hasExtra("image_description")) {
            val imageUrl: String = intent.getStringExtra("image_url")
            val imageTitle: String = intent.getStringExtra("image_title")
            val imageDescription: String = intent.getStringExtra("image_description")
            //
            detailsTitle.text = imageTitle
            detailsDescription.text = imageDescription
            Picasso.get().load(imageUrl)
                .into(detailsPost)
            //
            detailsPost.setOnClickListener {
                val intent: Intent = Intent(this, PostDetailsFullActivity::class.java)
                intent.putExtra("image_url", imageUrl)
                startActivity(intent)
            }
        }
    }
}