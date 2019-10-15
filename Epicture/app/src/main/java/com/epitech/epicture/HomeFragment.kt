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
import android.os.Handler
import android.os.Looper

class Photo {
    var id: String = "id" // "id" or "cover"
    var title: String = "title" // "title"
    var ups: Int = 0 // "ups"
    var downs: Int = 0 // "downs"
    var comment: Int = 0 // "comment_count"
    var views: Int = 0 // views
}

class HomeFragment : Fragment() {

    private val photos: ArrayList<Photo> = ArrayList()
    private var adapter: HomeFragmentAdapter = HomeFragmentAdapter(photos)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv_home)
        refreshHomeGallery()
        rv.adapter = this.adapter
        rv.layoutManager = LinearLayoutManager(this.activity)
        return view
    }

    private fun refreshHomeGallery() {
        var request = Request.Builder()
            .url(imgurClient.requestUrl)
            .header("Authorization", "Client-ID " + imgurClient.clientId)
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
                    println(item)
                    if (item.getBoolean("is_album")) {
                        photoItem.id = item.getString("cover")
                    } else {
                        photoItem.id = item.getString("id")
                    }
                    photoItem.title = item.getString("title")
                    photoItem.ups = item.getString("ups").toInt()
                    photoItem.downs = item.getString("downs").toInt()
                    photoItem.comment = item.getString("comment_count").toInt()
                    photoItem.views = item.getString("views").toInt()
                    photos.add(photoItem)
                    runOnUiThread(Runnable {
                        adapter.notifyDataSetChanged()
                    })
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute the request.")
            }
        })
    }

    private fun runOnUiThread(task: Runnable) {
        Handler(Looper.getMainLooper()).post(task)
    }

}