package com.epitech.epicture

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photo.view.*
import kotlinx.android.synthetic.main.photo_favorite.view.*
import kotlinx.android.synthetic.main.photo_favorite.view.imageButton
import kotlinx.android.synthetic.main.photo_favorite.view.imageGallery
import kotlinx.android.synthetic.main.photo_favorite.view.titleGallery
import kotlinx.android.synthetic.main.photo_profile.view.*
import okhttp3.*
import java.io.IOException

class FavoritesFragmentAdapter(var photos: ArrayList<Photo>) :
    RecyclerView.Adapter<FavoritesFragmentAdapter.FavoritesFragmentViewHolder>() {

    private var unfav : ArrayList<Photo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesFragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_favorite, parent, false)
        return FavoritesFragmentViewHolder(view)
    }

    override fun getItemCount() = photos.size

    fun favoritePicture(id: String) {
        val requestBody = RequestBody.create(null, "")
        val requestUrl = "https://api.imgur.com/3/image/" + id + "/favorite/"
        val client = OkHttpClient()
        var request = Request.Builder()
            .url(requestUrl)
            .post(requestBody)
            .addHeader("Authorization", "Bearer " + imgurClient.accessToken)
            .addHeader("cache-control", "no-cache")
            .build()
        FormBody.Builder().build()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                println(response)
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute the request.")
            }
        })
    }

    private fun isUnfav(photo: Photo) : Boolean {
        for (item in unfav) {
            if (item.id == photo.id) {
                return true
            }
        }
        return false
    }

    override fun onBindViewHolder(holder: FavoritesFragmentViewHolder, position: Int) {
        holder.view.titleGallery.text = photos[position].title
        Picasso.get().load("https://i.imgur.com/" + photos[position].id + ".jpg")
            .into(holder.view.imageGallery)

        holder.view.imageButton.setImageResource(android.R.drawable.btn_star_big_on)

        holder.view.imageButton.setOnClickListener {
            if (position < photos.size && isUnfav(photos[position])) {
                favoritePicture(photos[position].id)
                holder.view.imageButton.setImageResource(android.R.drawable.btn_star_big_on)
                for (item in unfav) {
                    if (item.id == photos[position].id) {
                        photos.add(item)
                        unfav.remove(item)
                    }
                }
            } else if (position < photos.size) {
                favoritePicture(photos[position].id)
                holder.view.imageButton.setImageResource(android.R.drawable.btn_star_big_off)
                unfav.add(photos[position])
                photos.removeAt(position)
            }
        }

        val description = photos[position].description
        val title = photos[position].title
        val pictureUrl = "https://i.imgur.com/" + photos[position].id + ".jpg"
        holder.view.imageGallery.setOnClickListener {
            val intent = Intent(holder.view.context, PostDetailsActivity::class.java)
            intent.putExtra("image_url", pictureUrl)
            intent.putExtra("image_title", title)
            intent.putExtra("image_description", description)
            holder.view.context.startActivity(intent)
        }
    }
    class FavoritesFragmentViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}