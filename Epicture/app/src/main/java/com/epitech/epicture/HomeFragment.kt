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
        refreshHomeGallery(imgurClient.requestUrl)
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
                    photoItem.ups = item.getString("ups").toInt()
                    photoItem.downs = item.getString("downs").toInt()
                    photoItem.comment = item.getString("comment_count").toInt()
                    photoItem.views = item.getString("views").toInt()
                    photos.add(photoItem)
                }
                runOnUiThread(Runnable {
                    println("tessssssssssst")
                    adapter.notifyDataSetChanged()
                })
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
                val intent =  Intent(activity, UploadActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.itemHot -> {
                photos.clear()
                Toast.makeText(context, "Load hot galleries !", Toast.LENGTH_SHORT).show()
                refreshHomeGallery(imgurClient.requestUrl)
                return true
            }
            R.id.itemTop -> {
                photos.clear()
                Toast.makeText(context, "Load top galleries !", Toast.LENGTH_SHORT).show()
                refreshHomeGallery(imgurClient.requestUrlTop)
                return true
            }
            R.id.itemViral -> {
                photos.clear()
                Toast.makeText(context, "Load viral galleries !", Toast.LENGTH_SHORT).show()
                refreshHomeGallery(imgurClient.requestUrlViral)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun runOnUiThread(task: Runnable) {
        Handler(Looper.getMainLooper()).post(task)
    }

}