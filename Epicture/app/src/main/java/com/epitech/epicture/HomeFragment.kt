package com.epitech.epicture

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import kotlinx.android.synthetic.main.photo.*
import kotlinx.android.synthetic.main.photo.view.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*


//import sun.jvm.hotspot.utilities.IntArray


/*Class to fetch JSONDataArray content*/
class Photo {
    var id: String = ""
    var title: String = ""
}

class HomeFragment : Fragment() {

    val requestUrl = "https://api.imgur.com/3/gallery/hot/time/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        fetchHomeGallery()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /*Class to display what Photo contains*/
    private class PhotoVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var photo: ImageView? = null
        internal var title: TextView? = null
    }

    private fun fetchHomeGallery()
    {
        var request = Request.Builder()
            .url(requestUrl)
            .header("Authorization", "Client-ID 979976772a2d967")
            .header("User-Agent", "Epicture")
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            /*The request is OK*/

            override fun onResponse(call: Call, response: Response) {
                /*Declare variable data to parse http request*/

                val jsonData = JSONObject(response.body()?.string())
                val jsonItems = jsonData.getJSONArray("data")
                val photos = ArrayList<Photo>()

                /*Loop through dataArray to put into Photo class*/

                for(items in 0 until (jsonItems.length() - 1)) {
                    val item = jsonItems.getJSONObject(items)
                    val photoItem = Photo()
                    if (item.getBoolean("is_album")) {
                        photoItem.id = item.getString("cover")
                    } else {
                        photoItem.id = item.getString("id")
                    }
                    photoItem.title = item.getString("title")
                    photos.add(photoItem)
                }

            }
            /*The request fails*/

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute the request.")
            }
        })
    }
}