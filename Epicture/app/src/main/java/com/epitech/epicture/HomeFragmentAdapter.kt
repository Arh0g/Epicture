package com.epitech.epicture

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HomeFragmentAdapter(val photos: HomeFragment.PhotoVH) : RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    }

    override fun getItemCount(): Int {
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}