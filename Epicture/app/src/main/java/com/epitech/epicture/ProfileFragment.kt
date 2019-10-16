package com.epitech.epicture

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ProfileFragment : Fragment() {

    var clientId: String = "979976772a2d967"
    var clientSecret: String = "2fd10421531b2478f628367988ee622d5c6ddb70"

    private var root: View? = null
    private var photos: ArrayList<Photo> = ArrayList()
    private var adapter: ProfileFragmentAdapter = ProfileFragmentAdapter(photos)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        //var rv = view.findViewById<RecyclerView>(R.id.rv_profile)
        fetchAvatarProfile()
        fetchPostNumberProfile()
        fetchReputationProfile()
        //rv.adapter = this.adapter
        //rv.layoutManager = LinearLayoutManager(this.activity)
        return view
    }

    private fun runOnUiThread(task: Runnable) {
        Handler(Looper.getMainLooper()).post(task)
    }

    fun fetchAvatarProfile() {
        var requestProfile = "https://api.imgur.com/3/account/" + imgurClient.accountUsername + "/avatar/"

        var request = Request.Builder()
            .url(requestProfile)
            .header("Authorization", "Bearer " + imgurClient.accessToken)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {

                val jsonData = JSONObject(response.body()?.string())
                val jsonItems = jsonData.getJSONObject("data")
                println(jsonItems)
                val itemIdentificator = jsonItems["avatar"].toString()

                runOnUiThread(Runnable {
                    profileName.text = imgurClient.accountUsername
                    Picasso.get().load(itemIdentificator).into(profileAvatar)
                })

            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute the request.")
            }
        })
    }

    fun fetchPostNumberProfile() {
        var requestProfile = "https://api.imgur.com/3/account/" + imgurClient.accountUsername+ "/images/"

        var request = Request.Builder()
            .url(requestProfile)
            .header("Authorization", "Bearer " + imgurClient.accessToken)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {

                val jsonData = JSONObject(response.body()?.string())
                val jsonItems = jsonData.getJSONArray("data")

                runOnUiThread(Runnable {
                    profilePostNumber.text = jsonItems.length().toString()
                    if (jsonItems.length() <= 1)
                        profilePostText.text = "post"
                })

            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute the request.")
            }
        })
    }

    fun fetchReputationProfile() {
        var requestProfile = "https://api.imgur.com/3/account/" + imgurClient.accountUsername

        var request = Request.Builder()
            .url(requestProfile)
            .header("Authorization", "Bearer " + imgurClient.accessToken)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {

                val jsonData = JSONObject(response.body()?.string())
                val jsonItems = jsonData.getJSONObject("data")

                runOnUiThread(Runnable {
                    val reputation = jsonItems["reputation"] as Int
                    val reputationType: String = jsonItems["reputation_name"].toString()

                    profileReputationNumber.text = reputation.toString()
                    if (reputation <= 1)
                        profileReputationText.text = "internet point"
                    profileReputationType.text = reputationType
                })

            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute the request.")
            }
        })
    }
}