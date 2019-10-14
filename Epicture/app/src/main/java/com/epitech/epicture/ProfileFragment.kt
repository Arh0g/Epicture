package com.epitech.epicture

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ProfileFragment : Fragment() {

    var clientId: String = "979976772a2d967"
    var clientSecret: String = "2fd10421531b2478f628367988ee622d5c6ddb70"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}