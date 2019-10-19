package com.epitech.epicture

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SearchFragment : Fragment() {

    private val photos: ArrayList<Photo> = ArrayList()
    private val fav: ArrayList<Photo> = ArrayList()
    private var adapter: HomeFragmentAdapter = HomeFragmentAdapter(photos, fav)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv_search)
        rv.adapter = this.adapter
        rv.layoutManager = LinearLayoutManager(this.activity)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        setHasOptionsMenu(true)
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_toolbar, menu);
        val searchItem = menu.findItem(R.id.menu_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    if (!p0.isNullOrBlank()) {
                        getSearchPhotos(p0)
                    }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }

            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getSearchPhotos(arg: String) {
        var requestSearch =
            "https://api.imgur.com/3/gallery/search/top?q=$arg"

        var request = Request.Builder()
            .url(requestSearch)
            .header("Authorization", "Bearer " + imgurClient.accessToken)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val jsonData = JSONObject(response.body()?.string())
                val jsonItems = jsonData.getJSONArray("data")

                if (jsonItems.length() == 0) {
                    runOnUiThread(Runnable {
                        Toast.makeText(
                            context,
                            "Search didn't matched.",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
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

    private fun runOnUiThread(task: Runnable) {
        Handler(Looper.getMainLooper()).post(task)
    }
}