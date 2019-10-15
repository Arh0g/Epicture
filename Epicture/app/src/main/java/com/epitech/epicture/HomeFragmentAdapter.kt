package com.epitech.epicture

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.photo.view.*
import com.squareup.picasso.Picasso

class HomeFragmentAdapter(private val photos: ArrayList<Photo>) :
    RecyclerView.Adapter<HomeFragmentAdapter.HomeFragmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo, parent, false)
        return HomeFragmentViewHolder(view)
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