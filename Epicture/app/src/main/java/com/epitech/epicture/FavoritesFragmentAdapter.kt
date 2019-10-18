package com.epitech.epicture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    fun addFavoritePicture() {

    }

    class FavoritesFragmentViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}