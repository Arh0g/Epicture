package com.epitech.epicture

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photo_favorite.view.*
import kotlinx.android.synthetic.main.photo_profile.view.*

class FavoritesFragmentAdapter(var photos: ArrayList<Photo>) :
    RecyclerView.Adapter<FavoritesFragmentAdapter.FavoritesFragmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesFragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_favorite, parent, false)
        return FavoritesFragmentViewHolder(view)
    }

    override fun getItemCount() = photos.size

    override fun onBindViewHolder(holder: FavoritesFragmentViewHolder, position: Int) {
        holder.view.titleGallery.text = photos[position].title
        Picasso.get().load("https://i.imgur.com/" + photos[position].id + ".jpg")
            .into(holder.view.imageGallery)

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
    class FavoritesFragmentViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}