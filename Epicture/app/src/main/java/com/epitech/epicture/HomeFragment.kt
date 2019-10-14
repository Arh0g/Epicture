package com.epitech.epicture

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Thread.sleep

class Photo {
    var id: String = "id"
    var title: String = "title"
}

class HomeFragment : Fragment() {

    val requestUrl = "https://api.imgur.com/3/gallery/hot/time/"
    val photos = ArrayList<Photo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv_home)

        fetchHomeGallery()
        sleep(1000) // ATTENTION A ENLEVER

        println(photos.size)
        rv.layoutManager = LinearLayoutManager(this.activity)
        rv.adapter = HomeFragmentAdapter(photos)
        return view
    }

    private fun fetchHomeGallery() {
        var request = Request.Builder()
            .url(requestUrl)
            .header("Authorization", "Client-ID 979976772a2d967")
            .header("User-Agent", "Epicture")
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {

                val jsonData = JSONObject(response.body()?.string())
                val jsonItems = jsonData.getJSONArray("data")

                for (items in 0 until (jsonItems.length() - 1)) {
                    val item = jsonItems.getJSONObject(items)
                    val photoItem = Photo()
                    if (item.getBoolean("is_album")) {
                        photoItem.id = item.getString("cover")
                    } else {
                        photoItem.id = item.getString("id")
                    }
                    photoItem.title = item.getString("title")
                    photos.add(photoItem)
                    println(photoItem.title)
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute the request.")
            }
        })
    }

}