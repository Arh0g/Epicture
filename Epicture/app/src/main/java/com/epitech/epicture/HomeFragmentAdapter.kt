package com.epitech.epicture

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.R



private class HomeFragmentAdapter(val photos: ArrayList<PhotoVH>) : RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    }

    override fun getItemCount() = photos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}