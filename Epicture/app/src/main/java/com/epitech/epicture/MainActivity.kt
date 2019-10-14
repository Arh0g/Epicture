package com.epitech.epicture

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.HttpUrl
import androidx.appcompat.widget.Toolbar
import android.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.Menu
import android.view.MenuItem
import okhttp3.OkHttpClient


class MainActivity : AppCompatActivity() {

    private val authentification = imgurAnthentification()

    private class imgurAnthentification {

        /*Variable declaration*/
        var clientId: String = "979976772a2d967"
        var clientSecret: String = "451d6d338eab5e4ad594d8116e3fb1d96b2f021d"
        val requestAuthentification = "api.imgur.com"

        var accessToken = ""
        var refreshToken = ""
        var username = ""

        val client: OkHttpClient = OkHttpClient.Builder().build()

        /*Member function declaration*/

        fun launchAnthentification(context: Context) {
            var request = HttpUrl.Builder()
                .scheme("https")
                .host(requestAuthentification)
                .addPathSegment("oauth2")
                .addPathSegment("authorize")
                .addQueryParameter("client_id", clientId)
                .addQueryParameter("response_type", "token")
            var intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(request.toString()))
            context.startActivity(intent)
        }

        fun getImgurResponse(requestResponse: String) {
            println(requestResponse)
        }
    }

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
                replaceFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_favorites -> {
                replaceFragment(FavoritesFragment())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    /*End of the functions*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navView.setSelectedItemId(R.id.nav_home)

        setSupportActionBar(findViewById(R.id.toolbar))
    }

    /*For toolbar */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.connection_button) {
            Toast.makeText(this, "Let's connect you !", Toast.LENGTH_LONG).show()
            authentification.launchAnthentification(this@MainActivity)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
