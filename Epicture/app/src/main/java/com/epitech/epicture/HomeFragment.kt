package com.epitech.epicture

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager


class Photo {
    var id: String = "id" // "id" or "cover"
    var title: String = "title" // "title"
    var ups: Int = 0 // "ups"
    var downs: Int = 0 // "downs"
    var comment: Int = 0 // "comment_count"
    var views: Int = 0 // views
    var description: String = "description"
}

class HomeFragment : Fragment() {

    private val photos: ArrayList<Photo> = ArrayList()
    private var fav: ArrayList<Photo> = ArrayList()
    private var adapter: HomeFragmentAdapter = HomeFragmentAdapter(photos, fav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv_home)
        refreshHomeGallery(imgurClient.requestUrl)
        fetchFavoritesPictures()
        rv.adapter = this.adapter
        rv.layoutManager = LinearLayoutManager(this.activity)
        return view
    }

    private fun refreshHomeGallery(requestUrl: String) {
        var request = Request.Builder()
            .url(requestUrl)
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
                    if (item.getBoolean("is_album")) {
                        photoItem.id = item.getString("cover")
                    } else {
                        photoItem.id = item.getString("id")
                    }
                    photoItem.title = item.getString("title")
                    photoItem.description = item.getString("description")
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
                    fav.add(photoItem)
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute the request.")
            }
        })
    }

    override fun onResume() {
        setHasOptionsMenu(true)
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemAddPicture -> {
                if (imgurClient.accessToken != "") {
                    val intent = Intent(activity, UploadActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "You are not connected.", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            R.id.itemHot -> {
                photos.clear()
                fav.clear()
                Toast.makeText(context, "Load hot galleries !", Toast.LENGTH_SHORT).show()
                refreshHomeGallery(imgurClient.requestUrl)
                fetchFavoritesPictures()
                return true
            }
            R.id.itemTop -> {
                photos.clear()
                fav.clear()
                Toast.makeText(context, "Load top galleries !", Toast.LENGTH_SHORT).show()
                refreshHomeGallery(imgurClient.requestUrlTop)
                fetchFavoritesPictures()
                return true
            }
            R.id.itemViral -> {
                photos.clear()
                fav.clear()
                Toast.makeText(context, "Load viral galleries !", Toast.LENGTH_SHORT).show()
                refreshHomeGallery(imgurClient.requestUrlViral)
                fetchFavoritesPictures()
                return true
            }
            R.id.itemLogout -> {
                if (imgurClient.accessToken != "") {
                    Toast.makeText(context, "You are logged out !", Toast.LENGTH_SHORT).show()
                    imgurClient.logoutClient()
                } else {
                    Toast.makeText(context, "You are already logout !", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.itemLogin -> {
                if (imgurClient.accessToken == "") {
                    val intentToWebView = Intent(context, LoginActivity::class.java)
                    startActivity(intentToWebView)
                } else {
                    Toast.makeText(context, "You are already logged in !", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    private fun runOnUiThread(task: Runnable) {
        Handler(Looper.getMainLooper()).post(task)
    }

}