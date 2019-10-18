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

class ProfileFragmentAdapter(var photos: ArrayList<Post>) :
    RecyclerView.Adapter<ProfileFragmentAdapter.ProfileFragmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileFragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_profile, parent, false)
        return ProfileFragmentViewHolder(view)
    }

    override fun getItemCount() = photos.size

    override fun onBindViewHolder(holder: ProfileFragmentViewHolder, position: Int) {
        holder.view.textPost.text = photos[position].title
        Picasso.get().load("https://i.imgur.com/" + photos[position].id + ".jpg")
            .into(holder.view.imagePost)
        val description = photos[position].description
        val title = photos[position].title
        val pictureUrl = "https://i.imgur.com/" + photos[position].id + ".jpg"
        holder.view.setOnClickListener {
            Toast.makeText(holder.view.context, "Go to see your post details !", Toast.LENGTH_SHORT).show()
            val intent: Intent = Intent(holder.view.context, PostDetailsActivity::class.java)
            intent.putExtra("image_url", pictureUrl)
            intent.putExtra("image_title", title)
            intent.putExtra("image_description", description)
            holder.view.context.startActivity(intent)
        }
    }

    class ProfileFragmentViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
