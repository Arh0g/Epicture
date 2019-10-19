package com.epitech.epicture

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class FavoritesFragment : Fragment() {

    private var photos: ArrayList<Photo> = ArrayList()
    private var adapter: FavoritesFragmentAdapter = FavoritesFragmentAdapter(photos)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        var rv = view.findViewById<RecyclerView>(R.id.rv_home)
        fetchFavoritesPictures()
        rv.adapter = this.adapter
        rv.layoutManager = LinearLayoutManager(this.activity)
        return view
    }

    private fun runOnUiThread(task: Runnable) {
        Handler(Looper.getMainLooper()).post(task)
    }

    private fun fetchFavoritesPictures() {
        var requestFavorites =
            "https://api.imgur.com/3/account/" + imgurClient.accountUsername + "/favorites/"

        var request = Request.Builder()
            .url(requestFavorites)
            .header("Authorization", "Bearer " + imgurClient.accessToken)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val jsonData = JSONObject(response.body()?.string())
                val jsonItems = jsonData.getJSONArray("data")

                if (jsonItems.length() == 0) {
                    Toast.makeText(
                        context,
                        "You don't have any favorite pictures.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                for (items in 0 until (jsonItems.length())) {
                    val item = jsonItems.getJSONObject(items)
                    val photoItem = Photo()
                    if (item.getBoolean("is_album")) {
                        photoItem.id = item.getString("cover")
                    } else {
                        photoItem.id = item.getString("id")
                    }
                    photoItem.title = item.getString("title")
                    if (photoItem.title.isNullOrBlank() || photoItem.description == "null")
                        photoItem.title = "No title"
                    photoItem.description = item.getString("description")
                    if (photoItem.description.isNullOrBlank() || photoItem.description == "null")
                        photoItem.description = "No description."
                    photoItem.ups = item.getString("ups").toInt()
                    photoItem.downs = item.getString("downs").toInt()
                    photoItem.comment = item.getString("comment_count").toInt()
                    photoItem.views = item.getString("views").toInt()
                    photos.add(photoItem)
                }
                runOnUiThread(Runnable {
                    adapter.notifyDataSetChanged()
                })
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute the request.")
            }
        })
    }
}