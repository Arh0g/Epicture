package com.epitech.epicture

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.post_detail_photo.*
import kotlinx.android.synthetic.main.post_details.*

class PostDetailsFullActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_detail_photo)

        if (intent.hasExtra("image_url")) {
            val imageUrl = intent.getStringExtra("image_url")
            Picasso.get().load(imageUrl)
                .into(detailsPostFull)
        }
    }
}