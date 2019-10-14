package com.epitech.epicture

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    val requestUrl = "https://api.imgur.com/3/gallery/hot/time/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv_home)

        val titles: ArrayList<String> = ArrayList()
        for (i in 1..100) {
            titles.add("SALUTE CA VA #$i")
        }

        rv.layoutManager = LinearLayoutManager(this.activity)
        rv.adapter = HomeFragmentAdapter(titles)
        return view
    }

}