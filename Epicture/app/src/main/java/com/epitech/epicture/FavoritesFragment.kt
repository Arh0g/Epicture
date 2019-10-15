package com.epitech.epicture

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class FavoritesFragment : Fragment() {

    fun fetchFavoritesPictures() {
        var requestFavorites = "https://api.imgur.com/3/account/" + imgurClient.accountUsername + "/favorites/"

        var request = Request.Builder()
            .url(requestFavorites)
            .header("Authorization", "Bearer " + imgurClient.accessToken)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {

                val jsonData = JSONObject(response.body()?.string())
                val jsonItems = jsonData.getJSONArray("data")

                runOnUiThread(Runnable {
                    if (jsonItems.length() == 0) {
                        Toast.makeText(context, "You don't have any favorite pictures.", Toast.LENGTH_SHORT).show()
                    }
                    println("---> Favorites")
                    println(jsonItems)
                })

            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute the request.")
            }
        })
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreate(savedInstanceState)
        fetchFavoritesPictures()
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun runOnUiThread(task: Runnable) {
        Handler(Looper.getMainLooper()).post(task)
    }
}