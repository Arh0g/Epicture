package com.epitech.epicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class imgurClient {

    companion object {
        const val requestUrl = "https://api.imgur.com/3/gallery/hot/time/"
        const val requestUrlViral = "https://api.imgur.com/3/gallery/hot/viral/"
        const val requestUrlTop = "https://api.imgur.com/3/gallery/hot/top/"
        const val clientId = "979976772a2d967"
        const val clientSecret = "f4fb596a2a59f7b622e15c316bcd0b0eaf9faf12"
        var accessToken: String = ""
        var refreshToken: String = ""
        var accountUsername: String = ""
        var accountIdentification: String = ""
        var isConnected: Boolean = false

        fun logoutClient() {
            accessToken = ""
            refreshToken = ""
            accountUsername = ""
            accountIdentification = ""
        }
    }
}

class MainActivity : AppCompatActivity() {

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        item -> when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_search -> {
                replaceFragment(SearchFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_person -> {
                if (imgurClient.accessToken != "") {
                    replaceFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true
                } else {
                    Toast.makeText(applicationContext,"You are not connected.",Toast.LENGTH_SHORT).show()
                }
            }
            R.id.nav_favorites -> {
                if (imgurClient.accessToken != "") {
                    replaceFragment(FavoritesFragment())
                    return@OnNavigationItemSelectedListener true
                } else {
                    Toast.makeText(applicationContext,"You are not connected.",Toast.LENGTH_SHORT).show()
                }
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!imgurClient.isConnected) {
            Toast.makeText(applicationContext,"You are not connected.",Toast.LENGTH_SHORT).show()
            val intentToWebView = Intent(this, LoginActivity::class.java)
            startActivityForResult(intentToWebView, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navView.setSelectedItemId(R.id.nav_home)
    }

    override fun onResume() {
        super.onResume()
    }
}
