package com.epitech.epicture

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.Toast
import kotlinx.android.synthetic.main.photo.view.*
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.RequestBody



class HomeFragmentAdapter(private val photos: ArrayList<Photo>) :
    RecyclerView.Adapter<HomeFragmentAdapter.HomeFragmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo, parent, false)
        return HomeFragmentViewHolder(view)
    }

    fun addFavoritePicture(id: String) {
        val requestBody = RequestBody.create(null,"")
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

    override fun onBindViewHolder(holder: HomeFragmentViewHolder, position: Int) {
        holder.view.titleGallery.text = photos[position].title
        holder.view.upsText.text = photos[position].ups.toString()
        holder.view.downsText.text = photos[position].downs.toString()
        holder.view.commentText.text = photos[position].comment.toString()
        holder.view.viewsText.text = photos[position].views.toString()
        Picasso.get().load("https://i.imgur.com/" + photos[position].id + ".jpg")
            .into(holder.view.imageGallery)
    }

    class HomeFragmentViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}