package com.epitech.epicture

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.photo.view.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photo.view.imageGallery
import kotlinx.android.synthetic.main.photo.view.titleGallery
import okhttp3.*
import android.R.drawable as drawable
import java.io.IOException
import okhttp3.RequestBody

class HomeFragmentAdapter(private val photos: ArrayList<Photo>, private val fav: ArrayList<Photo>) :
    RecyclerView.Adapter<HomeFragmentAdapter.HomeFragmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo, parent, false)
        return HomeFragmentViewHolder(view)
    }

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

    override fun getItemCount() = photos.size

    fun isInFavorite(position: Int): Boolean {
        for (item in fav) {
            if (item.id == photos[position].id) {
                return true
            }
        }
        return false
    }

    override fun onBindViewHolder(holder: HomeFragmentViewHolder, position: Int) {
        holder.view.titleGallery.text = photos[position].title
        holder.view.upsText.text = photos[position].ups.toString()
        holder.view.downsText.text = photos[position].downs.toString()
        holder.view.commentText.text = photos[position].comment.toString()
        holder.view.viewsText.text = photos[position].views.toString()
        Picasso.get().load("https://i.imgur.com/" + photos[position].id + ".jpg")
            .into(holder.view.imageGallery)

        if (isInFavorite(position)) {
            holder.view.imageButton.setImageResource(drawable.btn_star_big_on)
        }

        holder.view.imageButton.setOnClickListener {
            if (isInFavorite(position)) {
                favoritePicture(photos[position].id)
                holder.view.imageButton.setImageResource(drawable.btn_star_big_off)
                for (item in fav) {
                    if (item.id == photos[position].id) {
                        fav.remove(item)
                        break
                    }
                }
            } else {
                favoritePicture(photos[position].id)
                holder.view.imageButton.setImageResource(drawable.btn_star_big_on)
                fav.add(photos[position])
            }
        }

        val description = photos[position].description
        val title = photos[position].title
        val pictureUrl = "https://i.imgur.com/" + photos[position].id + ".jpg"
        holder.view.imageGallery.setOnClickListener {
            val intent: Intent = Intent(holder.view.context, PostDetailsActivity::class.java)
            intent.putExtra("image_url", pictureUrl)
            intent.putExtra("image_title", title)
            intent.putExtra("image_description", description)
            holder.view.context.startActivity(intent)
        }
    }

    class HomeFragmentViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}