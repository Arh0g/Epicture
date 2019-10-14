package com.epitech.epicture

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.photo.view.*

class HomeFragmentAdapter(private val title: ArrayList<String>) : RecyclerView.Adapter<HomeFragmentAdapter.HomeFragmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo, parent, false)
        return HomeFragmentViewHolder(view)
    }

    override fun getItemCount() = title.size

    override fun onBindViewHolder(holder: HomeFragmentViewHolder, position: Int) {
        if (holder.view.titleGallery != null) {
            holder.view.titleGallery.text = title[position]
        }
    }

    class HomeFragmentViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}